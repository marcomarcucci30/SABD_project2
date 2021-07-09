package queries.query3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class Query3SortAccumulator {

    private TreeMap<Double, List<Query3Result>> rankingMap;

    public Query3SortAccumulator(){
        this.rankingMap = new TreeMap<>(Collections.reverseOrder());
    }

    /**
     * Aggiorno la classifica
     * @param query3Result
     */
    public void add(Query3Result query3Result) {
        List<Query3Result> list = rankingMap.computeIfAbsent(query3Result.getDistance(), x -> new ArrayList<>());
        list.add(query3Result);
    }

    /**
     * merge dei due accumulatori
     * @param acc1
     */
    public void merge(Query3SortAccumulator acc1) {
        acc1.getRankingMap().forEach((key, valueList) -> {
            List<Query3Result> list = this.rankingMap.computeIfAbsent(key, x -> new ArrayList<>());
            for (Query3Result value: valueList){
                if (!list.contains(value))
                    list.add(value);
            }
        });
    }

    public TreeMap<Double, List<Query3Result>> getRankingMap() {
        return rankingMap;
    }

    public void setRankingMap(TreeMap<Double, List<Query3Result>> rankingMap) {
        this.rankingMap = rankingMap;
    }
}
