package queries.query1;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Classe che raccoglie i risultati di output della prima query
 */
public class Query1Result {

    private Map<String, Integer> map; //mappa (shiptype - n-shiptype)
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String cellId;

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Query1Result(Map<String, Integer> map){
        this.map = map;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    @Override
    public String toString() {
        return "Query1Outcome{" +
                "typeMap=" + map +
                ", date=" + startDate +
                ", cellId='" + cellId + '\'' +
                '}';
    }
}
