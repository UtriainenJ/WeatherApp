package fi.tuni.prog3.weatherapp;

/**
 * Interface for extracting data from the OpenWeatherMap API.
 */
public interface iAPI {

    /**
     * Returns the current weather for the given city.
     * @param loc Name of the location for which weather data should be fetched.
     * @return WeatherData.
     */
    public WeatherData getCurrentWeather(String loc);

    /**
     * Returns the current weather for the given coordinates.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return WeatherData.
     */
    public WeatherData getCurrentWeather(double lat, double lon);

    /**
     * Returns a forecast for the given coordinates.
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return ForecastDataHourly.
     */
    public ForecastDataHourly getForecastHourly(double lat, double lon);
    
    /**
     * Returns a forecast for the given city.
     * @param loc Name of the location for which weather data should be fetched.
     * @return ForecastDataHourly.
     */
    public ForecastDataHourly getForecastHourly(String loc);
}
