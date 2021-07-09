package queries.query3;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import utils.KafkaProperties;
import utils.ShipData;
import utils.SinkUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

public class Query3 {
    public static void run(DataStream<ShipData> dataStream){

        //datastream per processamento ogni ora
        DataStream<String> dataStreamOneHourOutput=dataStream.keyBy(ShipData::getTripId).window(TumblingEventTimeWindows.of(Time.hours(1), Time.minutes(48))).
                aggregate(new Query3Aggregator(), new Query3Process()).
                windowAll(TumblingEventTimeWindows.of(Time.hours(1), Time.minutes(48))).aggregate(new Query3SortAggregator()).
                map((MapFunction<TreeMap<Double, List<Query3Result>>, String>) SinkUtils::createCSVQuery3);

        //invio dei risultati su topic kafka
        Properties props = KafkaProperties.getFlinkProducerProperties("query3_output_producer");
        dataStreamOneHourOutput.addSink(new FlinkKafkaProducer<>(KafkaProperties.QUERY3_ONE_HOUR_TOPIC,
                (KafkaSerializationSchema<String>) (s, aLong) ->
                        new ProducerRecord<>(KafkaProperties.QUERY3_ONE_HOUR_TOPIC, s.getBytes(StandardCharsets.UTF_8)),
                props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)).setParallelism(1).name("q3_one_hour_kafka");
        //generazione benchmark
        //dataStreamOneHourOutput.addSink(new BenchmarkSink());

        //datastream per processamento ogni due ore
        DataStream<String> dataStreamTwoHourOutput=dataStream.keyBy(ShipData::getTripId).window(TumblingEventTimeWindows.of(Time.hours(2), Time.minutes(48))).
                aggregate(new Query3Aggregator(), new Query3Process()).
                windowAll(TumblingEventTimeWindows.of(Time.hours(2), Time.minutes(48))).aggregate(new Query3SortAggregator()).
                map((MapFunction<TreeMap<Double, List<Query3Result>>, String>) SinkUtils::createCSVQuery3);

        //invio dei risultati su topic kafka
        dataStreamTwoHourOutput.addSink(new FlinkKafkaProducer<>(KafkaProperties.QUERY3_TWO_HOUR_TOPIC,
                (KafkaSerializationSchema<String>) (s, aLong) ->
                        new ProducerRecord<>(KafkaProperties.QUERY3_TWO_HOUR_TOPIC, s.getBytes(StandardCharsets.UTF_8)),
                props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)).setParallelism(1).name("q3_two_hour_kafka");
        //generazione benchmark
        //dataStreamTwoHourOutput.addSink(new BenchmarkSink());

    }

}
