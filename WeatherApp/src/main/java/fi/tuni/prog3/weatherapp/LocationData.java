package fi.tuni.prog3.weatherapp;

/**
 * Used for getting latitude and longitude from city name with GSON.
 * @author Jerri Tarpio
 */
public class LocationData {
    private double lat;
    private double lon;

    /**
     * Get latitude
     * @return double latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Get longitude
     * @return double longitude
     */
    public double getLon() {
        return lon;
    }
}
