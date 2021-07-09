package queries.query3;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.time.ZoneId;


public class Query3Process
        extends ProcessWindowFunction<Query3Result, Query3Result, String, TimeWindow>{

    /**
     * Recupera timestamp di apertura e chiusura della finestra e la chiave e li inserisce nell'output
     * @param key chiave del raggruppamento byKey
     * @param context contesto
     * @param iterable elementi nella finestra
     * @param collector risultati in uscita
     */
    @Override
    public void process(String key, Context context, Iterable<Query3Result> iterable, Collector<Query3Result> collector) {
        Query3Result query3Result = iterable.iterator().next();
        query3Result.setStartDate(Instant.ofEpochMilli(context.window().getStart()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        query3Result.setEndDate(Instant.ofEpochMilli(context.window().getEnd()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        query3Result.setTripId(key);
        collector.collect(query3Result);
    }
}
