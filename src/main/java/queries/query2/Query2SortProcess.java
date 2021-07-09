package queries.query2;

import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

public class Query2SortProcess extends ProcessAllWindowFunction<Query2Result,
        List<TreeMap<Integer, List<Query2Result>>>, TimeWindow> {
    @Override
    public void process(Context context, Iterable<Query2Result> iterable, Collector<List<TreeMap<Integer,
            List<Query2Result>>>> collector) throws Exception {

        Query2Result query2Result = iterable.iterator().next();
        query2Result.setStartDate(Instant.ofEpochMilli(context.window().getStart()).
                atZone(ZoneId.systemDefault()).toLocalDateTime());

        //treemap per effettuare l'ordinamento
        TreeMap<Integer, List<Query2Result>> westAM = new TreeMap<>(Collections.reverseOrder());
        TreeMap<Integer, List<Query2Result>> westPM = new TreeMap<>(Collections.reverseOrder());
        TreeMap<Integer, List<Query2Result>> estAM = new TreeMap<>(Collections.reverseOrder());
        TreeMap<Integer, List<Query2Result>> estPM = new TreeMap<>(Collections.reverseOrder());
        for (Query2Result result: iterable){
            //aggiornamento valori treemap
            List<Query2Result> listWestAM = westAM.computeIfAbsent(result.getCountWestAM(), k -> new ArrayList<>());
            Query2Result westAMFiltered = new Query2Result(result.getCountWestAM(), 0, 0,
                    0, result.getCellId(), result.getStartDate());
            listWestAM.add(westAMFiltered);
            List<Query2Result> listWestPM = westPM.computeIfAbsent(result.getCountWestPM(), k -> new ArrayList<>());
            Query2Result westPMFiltered = new Query2Result(0, result.getCountWestPM(), 0,
                    0, result.getCellId(), result.getStartDate());
            listWestPM.add(westPMFiltered);
            List<Query2Result> listEstAM = estAM.computeIfAbsent(result.getCountEstAM(), k -> new ArrayList<>());
            Query2Result estAMFiltered = new Query2Result(0, 0, result.getCountEstAM(),
                    0, result.getCellId(), result.getStartDate());
            listEstAM.add(estAMFiltered);
            List<Query2Result> listEstPM = estPM.computeIfAbsent(result.getCountEstPM(), k -> new ArrayList<>());
            Query2Result estPMFiltered = new Query2Result(0, 0, 0,
                    result.getCountEstPM(), result.getCellId(), result.getStartDate());
            listEstPM.add(estPMFiltered);

        }
        //lista contentente le treemap aggiornate
        List<TreeMap<Integer, List<Query2Result>>> list = new ArrayList<>();
        list.add(westAM);
        list.add(westPM);
        list.add(estAM);
        list.add(estPM);
        collector.collect(list);
    }
}