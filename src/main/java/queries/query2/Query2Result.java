package queries.query2;

import java.time.LocalDateTime;

public class Query2Result {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String cellId;
    //dimensioni delle rispettive liste
    private int countWestAM;
    private int countWestPM;
    private int countEstAM;
    private int countEstPM;


    public Query2Result(Query2Accumulator query2Accumulator) {
        this.countWestAM = query2Accumulator.getShipIdsWestAM().size();
        this.countWestPM = query2Accumulator.getShipIdsWestPM().size();
        this.countEstAM = query2Accumulator.getShipIdsEstAM().size();
        this.countEstPM = query2Accumulator.getShipIdsEstPM().size();
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getCountWestAM() {
        return countWestAM;
    }

    public void setCountWestAM(int countWestAM) {
        this.countWestAM = countWestAM;
    }

    public int getCountWestPM() {
        return countWestPM;
    }

    public void setCountWestPM(int countWestPM) {
        this.countWestPM = countWestPM;
    }

    public int getCountEstAM() {
        return countEstAM;
    }

    public void setCountEstAM(int countEstAM) {
        this.countEstAM = countEstAM;
    }

    public int getCountEstPM() {
        return countEstPM;
    }

    public void setCountEstPM(int countEstPM) {
        this.countEstPM = countEstPM;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    @Override
    public String toString() {
        return "Query2Result{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", cellId='" + cellId + '\'' +
                ", countWestAM=" + countWestAM +
                ", countWestPM=" + countWestPM +
                ", countEstAM=" + countEstAM +
                ", countEstPM=" + countEstPM +
                '}';
    }


}
