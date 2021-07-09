package queries.query1;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.dropwizard.metrics.DropwizardMeterWrapper;
import org.apache.flink.metrics.Meter;
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
import java.util.Properties;

public class Query1 {
    /**
     *
     * @param dataStreamNoFilter DataStream in ingresso su cui eseguire il processamento
     */
    public static void run(DataStream<ShipData> dataStreamNoFilter){

        DataStream<ShipData> dataStream = dataStreamNoFilter //filtraggio Mediterraneo Occidentale
                .filter((FilterFunction<ShipData>) shipData -> shipData.getLon() < ShipData.getLonSeaSeparation());

        //datastream per processamento settimanale
        DataStream<String> dataStreamWeeklyOutput=dataStream.keyBy(ShipData::getCell)
                .window(TumblingEventTimeWindows.of(Time.days(7), Time.minutes(3648)))
                .aggregate(new Query1Aggregator(), new Query1Process()) //accumulazione dei dati e conteggio per finestra
                .map(SinkUtils::createCSVQuery1); //calcolo media e generazione risultati

        //invio dei risultati su topic kafka
        Properties props = KafkaProperties.getFlinkProducerProperties("query1_output_producer");
        dataStreamWeeklyOutput.addSink(new FlinkKafkaProducer<>(KafkaProperties.QUERY1_WEEKLY_TOPIC,
                (KafkaSerializationSchema<String>) (s, aLong) ->
                        new ProducerRecord<>(KafkaProperties.QUERY1_WEEKLY_TOPIC, s.getBytes(StandardCharsets.UTF_8)),
                props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)).name("q1_weekly_kafka");
        //generazione benchmark
        //dataStreamWeeklyOutput.addSink(new BenchmarkSink());

        //datastream per processamento mensile
        DataStream<String> dataStreamMonthlyOutput=dataStream.keyBy(ShipData::getCell).window(TumblingEventTimeWindows.of(Time.days(28), Time.minutes(23808)))
                .aggregate(new Query1Aggregator(), new Query1Process()) //accumulazione dei dati e conteggio per finestra
                .map(SinkUtils::createCSVQuery1); //calcolo media e generazione risultati

        //invio dei risultati su topic kafka
        dataStreamMonthlyOutput.addSink(new FlinkKafkaProducer<>(KafkaProperties.QUERY1_MONTHLY_TOPIC,
                (KafkaSerializationSchema<String>) (s, aLong) ->
                        new ProducerRecord<>(KafkaProperties.QUERY1_MONTHLY_TOPIC, s.getBytes(StandardCharsets.UTF_8)),
                props, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)).name("q1_monthly_kafka");
        //generazione benchmark
        //dataStreamMonthlyOutput.addSink(new BenchmarkSink());
    }

}
