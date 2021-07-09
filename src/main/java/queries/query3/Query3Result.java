package queries.query3;


import java.time.LocalDateTime;
import java.util.TreeMap;

public class Query3Result {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String tripId;
    private double distance;

    public Query3Result(Query3Accumulator query3Accumulator) {
        this.distance = query3Accumulator.getDistance();
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Query3Result{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", tripId='" + tripId + '\'' +
                ", distance=" + distance +
                '}';
    }
}
