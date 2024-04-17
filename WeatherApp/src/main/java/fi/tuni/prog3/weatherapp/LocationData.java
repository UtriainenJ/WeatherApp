package fi.tuni.prog3.weatherapp;

/**
 *
 * @author jerri
 * used for getting latitude and longitude from city name with GSON.
 */
public class LocationData {
    private double lat;
    private double lon;

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
}
