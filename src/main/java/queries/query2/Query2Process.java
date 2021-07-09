package queries.query2;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.time.ZoneId;


public class Query2Process
        extends ProcessWindowFunction<Query2Result, Query2Result, String, TimeWindow>{

    /**
     * Recupera timestamp di apertura e chiusura della finestra e la chiave e li inserisce nell'output
     * @param key chiave del raggruppamento byKey
     * @param context contesto
     * @param iterable elementi nella finestra
     * @param collector risultati in uscita
     */
    @Override
    public void process(String key, Context context, Iterable<Query2Result> iterable, Collector<Query2Result> collector) {
        Query2Result query2Result = iterable.iterator().next();
        query2Result.setStartDate(Instant.ofEpochMilli(context.window().getStart()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        query2Result.setEndDate(Instant.ofEpochMilli(context.window().getEnd()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        query2Result.setCellId(key);
        collector.collect(query2Result);
    }
}
