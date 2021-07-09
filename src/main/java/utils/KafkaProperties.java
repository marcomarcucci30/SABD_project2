package utils;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaProperties {
    public static final String QUERY1_WEEKLY_TOPIC = "query1_weekly_output";
    public static final String QUERY1_MONTHLY_TOPIC = "query1_monthly_output";
    public static final String QUERY2_WEEKLY_TOPIC = "query2_weekly_output";
    public static final String QUERY2_MONTHLY_TOPIC = "query2_monthly_output";
    public static final String QUERY3_ONE_HOUR_TOPIC = "query3_one_hour_output";
    public static final String QUERY3_TWO_HOUR_TOPIC = "query3_two_hour_output";
    public static final String[] LIST_TOPICS = {QUERY1_WEEKLY_TOPIC, QUERY1_MONTHLY_TOPIC, QUERY2_WEEKLY_TOPIC,
            QUERY2_MONTHLY_TOPIC, QUERY3_ONE_HOUR_TOPIC, QUERY3_TWO_HOUR_TOPIC};
    static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final String CONFIG = "kafka.properties";
    public static String TOPIC = "query";

    /**
     *
     * @param producerName
     * @return Producer properties
     */
    public static Properties getProducerProperties(String producerName){
        InputStream kafka_file = loader.getResourceAsStream(CONFIG);
        Properties props = new Properties();
        try {
            props.load(kafka_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producerName);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;

    }

    /**
     *
     * @param producerName
     * @return Flink Producer properties
     */
    public static Properties getFlinkProducerProperties(String producerName){
        InputStream kafka_file = loader.getResourceAsStream(CONFIG);
        Properties props = new Properties();
        try {
            props.load(kafka_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producerName);
        return props;

    }

    /**
     *
     * @param consumerName
     * @return Consumer properties
     */
    public static Properties getConsumerProperties(String consumerName){
        InputStream kafka_file = loader.getResourceAsStream(CONFIG);
        Properties props = new Properties();
        try {
            props.load(kafka_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "Consumer-Query2");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return props;
    }
}
