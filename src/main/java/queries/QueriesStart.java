package queries;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import queries.query1.Query1;
import queries.query2.Query2;
import queries.query3.Query3;
import utils.KafkaProperties;
import utils.Producer;
import utils.ShipData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Properties;

public class QueriesStart {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Properties props = KafkaProperties.getConsumerProperties("Query2Consumer");//consumer properties

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(KafkaProperties.TOPIC,
                new SimpleStringSchema(), props);
        //definizione timestamp e watermark
        consumer.assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofMillis(10*1000)));
        //consumer.assignTimestampsAndWatermarks(WatermarkStrategy.withIdleness(Duration.ofMillis(15*1000)));

        DataStream<ShipData> dataStream = env
                .addSource(consumer)
                .map((MapFunction<String, ShipData>) s -> {
                    String[] values = s.split(","); //split dataset line
                    String dateString = values[7];
                    Long timestamp = null;
                    for (SimpleDateFormat dateFormat : Producer.dateFormats) {
                        try {
                            timestamp = dateFormat.parse(dateString).getTime();

                            break;
                        } catch (ParseException ignored) {
                        }
                    }
                    if (timestamp == null)
                        throw new NullPointerException();
                    return new ShipData(values[0], Integer.parseInt(values[1]), Double.parseDouble(values[3]),
                            Double.parseDouble(values[4]), timestamp, values[10]);
                    //filter sull'area specificata dalla traccia
                }).filter((FilterFunction<ShipData>) shipData -> shipData.getLon() >= ShipData.getMinLon() &&
                        shipData.getLon() <= ShipData.getMaxLon() && shipData.getLat() >= ShipData.getMinLat() &&
                        shipData.getLat() <= ShipData.getMaxLat());

        Query1.run(dataStream);
        Query2.run(dataStream);
        Query3.run(dataStream);

        try {
            env.execute("Queries");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
