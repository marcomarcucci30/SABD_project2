package utils;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


public class Producer {
    static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final String CONFIG = "config.properties";
    private static long firstTimestamp = 0;
    private static final String COMMA_DELIMITER = ",";
    private static final long TOTAL_MILL_TIME = getReplayConfig();
    public static final SimpleDateFormat[] dateFormats = {new SimpleDateFormat("dd/MM/yy HH:mm"),
            new SimpleDateFormat("dd-MM-yy HH:mm")};
    private static final String FILE_NAME = "prj2_dataset.csv";
    private static String FIRST_JANUARY = "01/01";

    /**
     *
     * @return time replay config
     */
    public static int getReplayConfig() {
        InputStream config_file = loader.getResourceAsStream(CONFIG);
        Properties props = new Properties();
        try {
            props.load(config_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(props.getProperty("replay_time_milli"));
    }

    private static void calculateOffset(long firstTimestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        LocalDateTime ldt= Instant.ofEpochMilli(firstTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        int year = ldt.getYear();
        FIRST_JANUARY = FIRST_JANUARY+"/"+year+" 00:00";
        long long_first_jan = 0;
        try {
            long_first_jan = dateFormat.parse(FIRST_JANUARY).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long offset = firstTimestamp-long_first_jan;
        InputStream config_file = loader.getResourceAsStream(CONFIG);
        Properties props = new Properties();
        try {
            props.load(config_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.put("offset",offset);
    }

    /**
     * Ogni riga del dataset viene aggiunta ad una tree-map usando come chiave il timestamp.
     * L'ordinamento viene effettuato dalla tree-map
     * @return tree-map contenente le righe ordinate per timestamp
     */
    public static TreeMap<Long, List<String>> retrieve_dataset_line(){
        TreeMap<Long, List<String>> records = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean header = true;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (header){
                    header = false;
                    continue;
                }
                String[] values = line.split(COMMA_DELIMITER);
                String timestamp = values[7];
                Long long_timestamp = null;
                
                for (SimpleDateFormat dateFormat: dateFormats) {
                    try {
                        if (firstLine) {
                            firstTimestamp = dateFormat.parse(timestamp).getTime();
                            firstLine = false;
                        }
                        long_timestamp = dateFormat.parse(timestamp).getTime();
                        break;
                    } catch (ParseException ignored) { }
                }
                List<String> record_values = records.computeIfAbsent(long_timestamp, k -> new ArrayList<>());
                record_values.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Calcolo del tempo che deve intercorrere tra una tupla e l'altra ed invio dati
     * @param records tree-map contenente le righe ordinate per timestamp
     */
    public static void kafka_injector(TreeMap<Long, List<String>> records){
        Properties props = KafkaProperties.getProducerProperties("Producer");
        org.apache.kafka.clients.producer.Producer<Long, String> producer = new KafkaProducer<>(props);
        Long key_prev = null;
        double time_unit = (double) TOTAL_MILL_TIME / (double)(records.lastKey() - records.firstKey());
        for (Map.Entry<Long, List<String>> entry : records.entrySet()) {
            List<String> value = entry.getValue();
            Long key = entry.getKey();
            long sleep;
            if (key_prev != null) {
                 sleep = (long) ((key - key_prev) * time_unit);
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (String val: value){
                producer.send(new ProducerRecord<>(KafkaProperties.TOPIC,0, key, key, val), (m, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                    }
                });
            }
            key_prev = key;
        }
        producer.flush();
    }

    /**
     * Generazione del processo producer
     * @param args void
     */
    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        Instant start = Instant.now();
        TreeMap<Long, List<String>> records = retrieve_dataset_line();
        kafka_injector(records);
        Instant end = Instant.now();
        System.out.println("Injection completed in " + Duration.between(start, end).toMillis() + "ms");
    }


}
