package utils;

public class ShipData {
    String tripId;
    String shipId;
    double lat; //latitudine
    double lon; //longitudine
    long timestamp;
    String cell; //cell_id
    String shipType;
    String sea;//Mar Mediterraneo Occidentale e Orientale

    private final static double lonSeaSeparation = 11.797696;
    private static final double minLat = 32.0;
    private static final double maxLat = 45.0;
    private static final int stepsLat = 10;
    private static final double minLon = -6.0;
    private static final double maxLon = 37.0;
    private static final int stepsLon = 40;

    public ShipData(String shipId, int shipType, double lon, double lat, long timestamp, String tripId) {
        this.tripId = tripId;
        this.shipId = shipId;
        this.lon = lon;
        this.lat = lat;
        this.timestamp = timestamp;
        this.shipType = shipType(shipType);
        this.cell = evaluateCell(lat, lon);
        this.sea = evaluateSea(lon);
    }

    public ShipData(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
        this.cell = evaluateCell(lat, lon);
    }


    /**
     * Calcolo del Mar Occidentale e Orientale a partire dalla longitudine
     * @param lon longitudine
     * @return oriente o occidente
     */
    private String evaluateSea(double lon) {
        if (lon< getLonSeaSeparation()){
            return "WEST";
        } else {
            return "EST";
        }
    }


    /**
     * Calcolo della cella a partire da latitudine e longitudine
     * @param lat latitudine
     * @param lon longitudine
     * @return cellID
     */
    private String evaluateCell(double lat, double lon){
        char latId = 'A';
        int positionLat = (int)((lat-minLat)/((maxLat-minLat)/stepsLat));
        if (positionLat == 10)
            positionLat--;
        latId += positionLat;

        int lonId = 1;
        int positionLon = (int)((lon-minLon)/((maxLon-minLon)/stepsLon));
        if (positionLon == 40)
            positionLon--;
        lonId += positionLon;

        return ""+latId + lonId;
    }

    /**
     *
     * @param shipType intero che rappresenta lo ship type
     * @return Stringa contenente lo ship type
     */
    private String shipType(int shipType){
        if(shipType == 35){
            return String.valueOf(shipType);
        } else if (shipType >= 60 && shipType <= 69){
            return "60-69";
        } else if (shipType >= 70 && shipType <= 79){
            return "70-79";
        } else {
            return "others";
        }
    }
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public static double getLonSeaSeparation() {
        return lonSeaSeparation;
    }

    public static double getMinLat() {
        return minLat;
    }

    public static double getMaxLat() {
        return maxLat;
    }

    public static int getStepsLat() {
        return stepsLat;
    }

    public static double getMinLon() {
        return minLon;
    }

    public static double getMaxLon() {
        return maxLon;
    }

    public static int getStepsLon() {
        return stepsLon;
    }

    public String getSea() {
        return sea;
    }

    public void setSea(String sea) {
        this.sea = sea;
    }
}
