package utils;

import queries.query1.Query1Result;
import queries.query2.Query2Result;
import queries.query3.Query3Result;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SinkUtils {

    public static final String QUERY1_OUTPUT_WEEKLY="Results/Query1OutputWeekly.csv";
    public static final String QUERY1_OUTPUT_MONTHLY="Results/Query1OutputMonthly.csv";
    public static final String QUERY2_OUTPUT_WEEKLY="Results/Query2OutputWeekly.csv";
    public static final String QUERY2_OUTPUT_MONTHLY="Results/Query2OutputMonthly.csv";
    public static final String QUERY3_OUTPUT_ONE_HOUR ="Results/Query3OutputOneHour.csv";
    public static final String QUERY3_OUTPUT_TWO_HOUR ="Results/Query3OutputTwoHour.csv";

    public final static String[] LIST_OUTPUT= {QUERY1_OUTPUT_WEEKLY, QUERY1_OUTPUT_MONTHLY, QUERY2_OUTPUT_WEEKLY,
            QUERY2_OUTPUT_MONTHLY, QUERY3_OUTPUT_ONE_HOUR, QUERY3_OUTPUT_TWO_HOUR};

    /**
     *
     * @param query1Result Risultati da stampare sul file
     * @return stringa contente i risultati formattati
     */
    public static String createCSVQuery1(Query1Result query1Result){
        StringBuilder builder = new StringBuilder();
        builder.append(query1Result.getStartDate());
        builder.append(",");
        builder.append(query1Result.getCellId());
        long daysBetween = ChronoUnit.DAYS.between(query1Result.getStartDate(), query1Result.getEndDate());
        query1Result.getMap().forEach((k, v) -> {
            builder.append(",");
            builder.append(k);
            builder.append(",");
            builder.append((double)v/daysBetween);
        });
        return builder.toString();
    }

    /**
     *
     * @param list Risultati da stampare sul file
     * @return stringa contente i risultati formattati
     */
    public static String createCSVQuery2(List<TreeMap<Integer, List<Query2Result>>> list){
        StringBuilder builder = new StringBuilder();
        builder.append(list.get(0).firstEntry().getValue().get(0).getStartDate().toLocalDate());
        builder.append(",");
        builder.append("West Mediterranean");
        builder.append(",");
        builder.append("AM");
        builder.append(",");
        rankingBuilder(list, 0, builder);

        builder.append(",");
        builder.append("PM");
        builder.append(",");
        rankingBuilder(list, 1, builder);

        builder.append("\n");
        builder.append(list.get(0).firstEntry().getValue().get(0).getStartDate().toLocalDate());
        builder.append(",");
        builder.append("Est Mediterranean");
        builder.append(",");
        builder.append("AM");
        builder.append(",");
        rankingBuilder(list, 2, builder);

        builder.append(",");
        builder.append("PM");
        builder.append(",");
        rankingBuilder(list, 3, builder);

        return String.valueOf(builder);
    }

    /**
     *
     * @param treeMap Risultati da stampare sul file
     * @return stringa contente i risultati formattati
     */
    public static String createCSVQuery3(TreeMap<Double, List<Query3Result>> treeMap){
        StringBuilder builder = new StringBuilder();
        builder.append(treeMap.firstEntry().getValue().get(0).getStartDate());
        builder.append(",");
        int count = 0;
        for (int i=0;;i++){//Solo per scorrere le TreeMap
            if (count == 5)
                break;
            if (i >= treeMap.size()){
                break;
            }
            Optional<List<Query3Result>> first = treeMap.values()
                    .stream()
                    .skip(i)
                    .findFirst();
            if (first.isPresent()) {
                for (Query3Result result: first.get()){
                    if (count == 5)
                        break;
                    count++;
                    builder.append(result.getTripId());
                    builder.append(",");
                    builder.append(result.getDistance());
                    builder.append(",");
                }
            }
        }
        builder.deleteCharAt(builder.length()-1);
        return String.valueOf(builder);
    }

    private static void rankingBuilder(List<TreeMap<Integer, List<Query2Result>>> list, int index, StringBuilder builder){
        boolean atLest = false;
        int count = 0;
        for (int i=0; ;i++){
            if (count == 3)
                break;
            if (i >=  list.get(index).size()){
                break;
            }
            Optional<List<Query2Result>> first = list.get(index).values()
                    .stream()
                    .skip(i)
                    .findFirst();//valore associato al primo elemento della treemap

            if (first.isPresent()) {
                for (Query2Result result: first.get()){
                    if (count == 3)//se ho più di 3 elementi termino la classifica
                        break;
                    count++;
                    atLest = true;
                    String cellId = result.getCellId();
                    builder.append(cellId);
                    builder.append("-");
                }
            }
        }
        if (atLest)
            builder.deleteCharAt(builder.length()-1);
    }

}
