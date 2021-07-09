package queries.query3;

import org.apache.flink.api.common.functions.AggregateFunction;
import utils.ShipData;

public class Query3Aggregator implements AggregateFunction<ShipData, Query3Accumulator, Query3Result> {

    @Override
    public Query3Accumulator createAccumulator() {
        return new Query3Accumulator();
    }

    @Override
    public Query3Accumulator add(ShipData shipData, Query3Accumulator query3Accumulator) {
        query3Accumulator.add(shipData.getLon(), shipData.getLat());//aggiornamento dell'accumulator
        return query3Accumulator;
    }

    @Override
    public Query3Result getResult(Query3Accumulator query3Accumulator) {
        return new Query3Result(query3Accumulator);//restituzione dei risultati
    }
    @Override
    public Query3Accumulator merge(Query3Accumulator query3Accumulator, Query3Accumulator acc1) {
        query3Accumulator.merge(acc1);//merge degli accumulator
        return query3Accumulator;
    }
}
