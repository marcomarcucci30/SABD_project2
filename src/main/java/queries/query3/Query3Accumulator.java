package queries.query3;

import java.io.Serializable;

import org.apache.lucene.spatial.util.GeoDistanceUtils;

public class Query3Accumulator implements Serializable {

    private double lon;//longitudine della tupla precedente
    private double lat;//latitudine della tupla precedente
    private double distance;//distanza calcolata con la formula di Vincenty

    /**
     * Costruttore: genera la distanza da aggiornare
     */
    public Query3Accumulator(){
        this.lon = Double.NaN;
        this.lat = Double.NaN;
        this.distance = 0.0;
    }

    /**
     *
     * @param lon longitudine della tupla considerata
     * @param lat latitudine della tupla considerata
     */
    public void add(double lon, double lat) {
        if(!Double.isNaN(getLon()) && !Double.isNaN(getLat())){
            //double linearDistance = GeoDistanceUtils.linearDistance(new double[]{getLon(), getLat()}, new double[]{lon, lat});
            double linearDistance = GeoDistanceUtils.vincentyDistance(getLon(), getLat(), lon, lat);
            setDistance(linearDistance);
        }
        setLon(lon);
        setLat(lat);
    }

    public void merge(Query3Accumulator acc2){
        setDistance(getDistance()+acc2.getDistance());
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
