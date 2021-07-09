package queries.query2;

import benchmarks.BenchmarkSink;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
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

public class Query2 {

    public static void run(DataStream<ShipData> dataStream){
        //datastream per processamento settimanale
        DataStream<String> dataStreamWeeklyOutput=dataStream.keyBy(ShipData::getCell).window(TumblingEventTimeWindows.of(Time.days(7), Time.minutes(3648))).
                aggregate(new Query2Aggregator(), new Query2Process()).
                windowAll(TumblingEventTimeWindows.of(Time.days(7), Time.minutes(3648))).process(new Query2SortProcess()).
                map((MapFunction<List<TreeMap<Integer, List<Query2Result>>>, String>) SinkUtils::createCSVQuery2);

        Properties props = KafkaProperties.getFlinkProducerProperties("query2_output_producer");
        //invio dei risultati su topic kafka
        dataStreamWeeklyOutput.addSink(new FlinkKafkaProducer<>(KafkaProperties.QUERY2_WEEKLY_TOPIC,
                (KafkaSerializationSchema<String>) (s, aLong) ->
                        new ProducerRecord<>(KafkaProperties.QUERY2_WEEKLY_TOPIC, s.getBytes(StandardCharsets.UTF_8)),
                props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)).name("q2_weekly_kafka");
        //generazione benchmark
        //dataStreamWeeklyOutput.addSink(new BenchmarkSink());

        //datastream per processamento mensile
        DataStream<String> dataStreamMonthlyOutput=dataStream.keyBy(ShipData::getCell).window(TumblingEventTimeWindows.of(Time.days(28), Time.days(12))).
                aggregate(new Query2Aggregator(), new Query2Process()).
                windowAll(TumblingEventTimeWindows.of(Time.days(28), Time.minutes(23808))).process(new Query2SortProcess()).
                map((MapFunction<List<TreeMap<Integer, List<Query2Result>>>, String>) SinkUtils::createCSVQuery2);

        //invio dei risultati su topic kafka
        dataStreamMonthlyOutput.addSink(new FlinkKafkaProducer<>(KafkaProperties.QUERY2_MONTHLY_TOPIC,
                (KafkaSerializationSchema<String>) (s, aLong) ->
                        new ProducerRecord<>(KafkaProperties.QUERY2_MONTHLY_TOPIC, s.getBytes(StandardCharsets.UTF_8)),
                props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)).name("q2_monthly_kafka");
        //generazione benchmark
        //dataStreamMonthlyOutput.addSink(new BenchmarkSink());
    }


}





