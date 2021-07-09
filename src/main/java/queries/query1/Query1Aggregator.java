package queries.query1;

import utils.ShipData;
import org.apache.flink.api.common.functions.AggregateFunction;

public class Query1Aggregator implements AggregateFunction<ShipData, Query1Accumulator, Query1Result> {

    @Override
    public Query1Accumulator createAccumulator() {
        return new Query1Accumulator();
    }

    @Override
    public Query1Accumulator add(ShipData shipData, Query1Accumulator query1Accumulator) {
        query1Accumulator.add(shipData.getShipType(), 1); //aggiornamento dell'accumulator
        return query1Accumulator;
    }

    @Override
    public Query1Accumulator merge(Query1Accumulator acc1, Query1Accumulator acc2) {
        acc1.merge(acc2); //merge degli accumulator
        return acc1;
    }

    @Override
    public Query1Result getResult(Query1Accumulator accumulator) {
        return new Query1Result(accumulator.getMap()); //restituzione dei risultati
    }

}
