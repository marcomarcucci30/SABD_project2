package utils;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Scanner;

public class Consumer {
    /**
     * Genera i consumer thread legati ai topic di output
     */
    public static class ConsumerThread extends Thread {
        String topic;
        String filename;
        private static boolean running = true;

        public ConsumerThread(String topic, String filename){
            this.topic = topic;
            this.filename = filename;
        }
        public void run(){
            Consumer.consumer(topic, filename, true);
        }
        public void stop_thread() {
            running = false;
        }
    }

    static ClassLoader loader = Thread.currentThread().getContextClassLoader();

    /**
     * Funzione eseguita dal thread. Legge dal topic e genera un CSV in uscita
     * @param topic topic Kafka
     * @param output path del file CSV
     * @param firstLap booleano che permette l'inserimento dell'header
     */
    public static void consumer(String topic, String output, boolean firstLap){
        InputStream kafka_file = loader.getResourceAsStream("kafka.properties");
        Properties props = new Properties();
        try {
            props.load(kafka_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "flink");
        // exactly once semantic
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        org.apache.kafka.clients.consumer.Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        try {
            while (ConsumerThread.running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                if (!records.isEmpty()) {
                    File file = new File(output);

                    if (!file.exists()) {
                        // crea il file se non esiste
                        file.createNewFile();
                    }

                    FileWriter writer = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(writer);
                    if (firstLap){
                        String header = insertHeader(topic);
                        bw.append(header);
                        bw.append("\n");

                        firstLap=false;
                    }
                    for (ConsumerRecord<String, String> record : records) {
                        bw.append(record.value());
                        bw.append("\n");

                    }
                    bw.close();
                    writer.close();
                }
            }
        } catch (IOException e) {
        } finally {
            consumer.close();
        }
    }

    /**
     * Funzione per l'inserimento dell'header nei CSV a seconda del topic da cui il consumer preleva i record
     * @param topic topic Kafka
     * @return header
     */
    private static String insertHeader(String topic) {
        StringBuilder builder = new StringBuilder();
        switch (topic){
            case KafkaProperties.QUERY1_WEEKLY_TOPIC:
            case KafkaProperties.QUERY1_MONTHLY_TOPIC:
                builder.append("timestamp").append(",").append("id_cella").append(",").append("ship_t35").append(",")
                        .append("avg_t35").append(",").append("ship_t_60_69").append(",").append("avg_60_69")
                        .append(",").append("ship_t_70_79").append(",").append("avg_70_79").append(",")
                        .append("ship_t_others").append(",").append("avg_others");
                break;
            case KafkaProperties.QUERY2_WEEKLY_TOPIC:
            case KafkaProperties.QUERY2_MONTHLY_TOPIC:
                builder.append("timestamp").append(",").append("sea").append(",").append("slot_a").append(",")
                        .append("rank_a").append(",").append("slot_p").append(",").append("rank_p");
                break;
            case KafkaProperties.QUERY3_ONE_HOUR_TOPIC:
            case KafkaProperties.QUERY3_TWO_HOUR_TOPIC:
                builder.append("timestamp").append(",").append("trip_1").append(",").append("rating_1").append(",")
                        .append("trip_2").append(",").append("rating_2").append(",").append("trip_3").append(",")
                        .append("rating_3").append(",").append("trip_4").append(",").append("rating_4").append(",")
                        .append("trip_5").append(",").append("rating_5");
                break;
        }
        return builder.toString();

    }

    /**
     * Generazione dei thread consumer
     * @param args void
     */
    public static void main(String[] args) throws InterruptedException {
        Logger.getRootLogger().setLevel(Level.OFF);
        ArrayList<ConsumerThread> consumers = new ArrayList<>();
        for (int i = 0; i < KafkaProperties.LIST_TOPICS.length; i++) {
            ConsumerThread consumer = new ConsumerThread(KafkaProperties.LIST_TOPICS[i],
                    SinkUtils.LIST_OUTPUT[i]);
            consumer.start();
            consumers.add(consumer);
        }
        Scanner scanner = new Scanner(System.in);
        // Attendere che l'utente digiti qualcosa
        scanner.next();
        System.out.println("Sending shutdown signal to consumers");
        // stop consumers
        for (ConsumerThread consumer : consumers) {
            consumer.stop_thread();
        }

        for (ConsumerThread consumer : consumers) {
            consumer.join();
        }

    }
}
