package queries.query1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Query1Accumulator implements Serializable {

    //mappa (shiptype - n-shiptype)
    private Map<String, Integer> map;

    /**
     * Costruttore: genera una mappa da aggiornare
     */
    public Query1Accumulator(){
        this.map = new HashMap<>();
        this.map.put("35", 0);
        this.map.put("60-69", 0);
        this.map.put("70-79", 0);
        this.map.put("others", 0);
    }

    /**
     *
     * @param shipType stringa contenente il codice relativo al tipo di nave
     * @param value 1
     */
    public void add(String shipType, Integer value){
        Integer count = map.get(shipType);
        if(count == null){
            count = value;
        }else{
            count++;
        }
        map.put(shipType, count);

    }

    /**
     * @param acc2 accumulatore da unire ad acc1
     */
    public void merge(Query1Accumulator acc2){
        for (Map.Entry<String, Integer> entry : acc2.getMap().entrySet()) {
            String shipType = entry.getKey();
            Integer countAcc2 = entry.getValue();
            if (countAcc2 == null) //evita NullPointer
                countAcc2 = 0;
            Integer countAcc1 = getMap().get(shipType);
            if (countAcc1 == null) //evita NullPointer
                countAcc1 = 0;
            add(shipType, countAcc1+countAcc2); //aggiornamento
        }
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
