package queries.query2;

import org.apache.flink.api.common.functions.AggregateFunction;
import utils.ShipData;

public class Query2Aggregator implements AggregateFunction<ShipData, Query2Accumulator, Query2Result> {

    @Override
    public Query2Accumulator createAccumulator() {
        return new Query2Accumulator();
    }

    @Override
    public Query2Accumulator add(ShipData shipData, Query2Accumulator query2Accumulator) {
        query2Accumulator.add(shipData.getShipId(), shipData.getSea(), shipData.getTimestamp());//aggiornamento dell'accumulator
        return query2Accumulator;
    }

    @Override
    public Query2Result getResult(Query2Accumulator query2Accumulator) {
        return new Query2Result(query2Accumulator);//restituzione dei risultati
    }

    @Override
    public Query2Accumulator merge(Query2Accumulator query2Accumulator, Query2Accumulator acc2) {
        query2Accumulator.merge(acc2);//merge degli accumulator
        return query2Accumulator;
    }


}
