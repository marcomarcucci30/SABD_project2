package queries.query2;

import java.io.Serializable;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class Query2Accumulator implements Serializable {

    private List<String> shipIdsWestAM;
    private List<String> shipIdsWestPM;
    private List<String> shipIdsEstAM;
    private List<String> shipIdsEstPM;

    /**
     * Costruttore: genera le liste da aggiornare
     */
    public Query2Accumulator(){
        this.shipIdsWestAM = new ArrayList<>();
        this.shipIdsWestPM = new ArrayList<>();
        this.shipIdsEstAM = new ArrayList<>();
        this.shipIdsEstPM = new ArrayList<>();
    }

    /**
     *
     * @param shipId stringa contenente l'id della tratta considerata
     * @param sea stringa contenente l'indicazione del Mare attraversato
     * @param date timestamp relativo alla tupla considerata
     */
    public void add(String shipId, String sea, long date){
        LocalDateTime localDateTime = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime noon = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.NOON);
        LocalDateTime midnight = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIDNIGHT);

        if (sea.equals("WEST")){
            //verifica l'orario in cui la nave attraversa la cella del Mar Occidentale
            if ((localDateTime.isEqual(midnight) || localDateTime.isAfter(midnight)) && localDateTime.isBefore(noon)){
                if (!shipIdsWestAM.contains(shipId)){
                    shipIdsWestAM.add(shipId);
                }
            }else {
                if (!shipIdsWestPM.contains(shipId)) {
                    shipIdsWestPM.add(shipId);
                }
            }
        } else {
            //verifica l'orario in cui la nave attraversa la cella del Mar Orientale
            if ((localDateTime.isEqual(midnight) || localDateTime.isAfter(midnight)) && localDateTime.isBefore(noon)){
                if (!shipIdsEstAM.contains(shipId)){
                    shipIdsEstAM.add(shipId);
                }
            }else {
                if (!shipIdsEstPM.contains(shipId)) {
                    shipIdsEstPM.add(shipId);
                }
            }
        }

    }

    /**
     * @param acc accumulatore da unire ad acc1
     */
    public void merge(Query2Accumulator acc){
        //aggiornamento liste
        for (String val: acc.getShipIdsWestAM()){
            if (!this.shipIdsWestAM.contains(val)){
                this.shipIdsWestAM.add(val);
            }
        }

        for (String val: acc.getShipIdsWestPM()){
            if (!this.shipIdsWestPM.contains(val)){
                this.shipIdsWestPM.add(val);
            }
        }

        for (String val: acc.getShipIdsEstAM()){
            if (!this.shipIdsEstAM.contains(val)){
                this.shipIdsEstAM.add(val);
            }
        }

        for (String val: acc.getShipIdsEstPM()){
            if (!this.shipIdsEstPM.contains(val)){
                this.shipIdsEstPM.add(val);
            }
        }

    }

    public List<String> getShipIdsWestAM() {
        return shipIdsWestAM;
    }

    public void setShipIdsWestAM(List<String> shipIdsWestAM) {
        this.shipIdsWestAM = shipIdsWestAM;
    }

    public List<String> getShipIdsWestPM() {
        return shipIdsWestPM;
    }

    public void setShipIdsWestPM(List<String> shipIdsWestPM) {
        this.shipIdsWestPM = shipIdsWestPM;
    }

    public List<String> getShipIdsEstAM() {
        return shipIdsEstAM;
    }

    public void setShipIdsEstAM(List<String> shipIdsEstAM) {
        this.shipIdsEstAM = shipIdsEstAM;
    }

    public List<String> getShipIdsEstPM() {
        return shipIdsEstPM;
    }

    public void setShipIdsEstPM(List<String> shipIdsEstPM) {
        this.shipIdsEstPM = shipIdsEstPM;
    }
}
