package queries.query3;

import org.apache.flink.api.common.functions.AggregateFunction;

import java.util.List;
import java.util.TreeMap;

public class Query3SortAggregator implements AggregateFunction<Query3Result, Query3SortAccumulator, TreeMap<Double, List<Query3Result>>> {
    @Override
    public Query3SortAccumulator createAccumulator() {
        return new Query3SortAccumulator();
    }

    @Override
    public Query3SortAccumulator add(Query3Result query3Result, Query3SortAccumulator query3SortAccumulator) {
        query3SortAccumulator.add(query3Result);
        return query3SortAccumulator;
    }

    @Override
    public TreeMap<Double,List<Query3Result>> getResult(Query3SortAccumulator query3SortAccumulator) {
        return query3SortAccumulator.getRankingMap();
    }

    @Override
    public Query3SortAccumulator merge(Query3SortAccumulator query3SortAccumulator, Query3SortAccumulator acc1) {
        query3SortAccumulator.merge(acc1);
        return query3SortAccumulator;
    }
}
