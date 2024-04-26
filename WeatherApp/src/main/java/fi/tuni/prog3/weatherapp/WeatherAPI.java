package fi.tuni.prog3.weatherapp;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.util.Pair;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Used for interacting with openweathermap.org weather API.
 * @author jerri
 */
public class WeatherAPI implements iAPI {
    private String locationActive;
    private String units;
    private List<String> locationFavorites;
    private List<String> locationHistory;
    private transient WeatherData wd;
    private transient ForecastDataHourly fdh;
    private transient ForecastDataDaily fdd;

    /**
     * Constructor. Default location is Raisio, and default unit is metric.
     * @throws Exception
     */
    public WeatherAPI() throws Exception {
        this.locationActive = "Raisio";
        this.units = "metric";
        this.locationFavorites = new ArrayList<>();
        this.locationHistory = new ArrayList<>();
    }

    /**
     * Set a location as active before calling API
     * @param loc location
     * @return this
     */
    public WeatherAPI setLocationActive(String loc) {
        this.locationActive = loc;
        return this;
    }

    /**
     * Get active location
     * @return String active location
     */
    public String getLocationActive() {
        return locationActive;
    }

    /**
     * Get favourited locations
     * @return List of favourites
     */
    public List<String> getLocationFavorites() {
        return locationFavorites;
    }

    /**
     * Get searched locations
     * @return History list
     */
    public List<String> getLocationHistory() {
        return locationHistory;
    }
   
    /**
     * Add location to favourites
     * @param loc location
     */
    public void addToFavorites(String loc) {
        if(!locationFavorites.contains(loc)) {
            locationFavorites.add(0, loc);
        }
    }
    
    /**
     * Remove location from favourites
     * @param loc location
     */
    public void removeFromFavorites(String loc) {
        if(locationFavorites.contains(loc)) {
            locationFavorites.remove(loc);
        }
    }
 
    /**
     * Clear location history
     */
    public void clearBrowsingHistory() {
        this.locationHistory.clear();
    }
    
    /**
     * Get WeatherData object
     * @return WeatherData
     */
    public WeatherData getWeather() {
        return wd;
    }
    
    /**
     * Get ForecastDataHourly object
     * @return ForecastDataHourly
     */
    public ForecastDataHourly getForecastHourly() {
        return fdh;
    }
    
    /**
     * Get ForecastDataDaily object
     * @return ForecastDataDaily
     */
    public ForecastDataDaily getForecastDaily() {
        return fdd;
    }
    
    /**
     * Set units used by entire program, metric or imperial
     * @param unitSystem
     */
    public void setUnits(String unitSystem) {
        switch(unitSystem.toLowerCase()) {
            case "m":
            case "metric":
                units = "metric";
                wd.setUnits(units);
                fdd.setUnits(units);
                fdh.setUnits(units);
                
                break;
            case "i":
            case "imperial":
                units = "imperial";
                wd.setUnits(units);
                fdd.setUnits(units);
                fdh.setUnits(units);
                break;
            default:
                System.out.println("Invalid unit system" + unitSystem);
        }
    }
    
    /**
     * Change from imperial to metric or vice versa
     */
    public void switchUnits() {
        if("metric".equals(units)) {
            setUnits("imperial");
        } else {
            setUnits("metric");
        }
    }

    /**
     * Get unit name
     * @return String unit name
     */
    public String getUnit() {
        switch(units) {
            case "imperial":
                return "Imperial";
            case "metric":
                return "Metric";
            default:
                throw new IllegalStateException("No Units set");
        }
    }
    
    /**
     * Get wind speed unit corresponding to unit system
     * @return String wind speed unit
     */
    public String getUnitWind() {
        switch(units) {
            case "imperial":
                return "mph";
            case "metric":
                return "m/s";
            default:
                throw new IllegalStateException("No Units set");
        }
    }
    
    /**
     * Get rain amount unit corresponding to unit system
     * @return String rain amount unit
     */
    public String getUnitRain() {
        switch(units) {
            case "imperial":
                return String.valueOf('"');
            case "metric":
                return "mm";
            default:
                throw new IllegalStateException("No Units set");
        }
    }
    
    /**
     * Get temperature unit corresponding to unit system
     * @return String temperature unit
     */
    public String getUnitTemp() {
        switch(units) {
            case "imperial":
                return "°F";
            case "metric":
                return "°C";
            default:
                throw new IllegalStateException("No Units set");
        }
    }

    /**
     * Calls API to retrieve weather data to use
     * @return boolean whether call was successful
     */
    public boolean getData() {
        try {
            this.wd = getCurrentWeather(locationActive);
            this.fdh = getForecastHourly(locationActive);
            this.fdd = getForecastDaily(locationActive);
            this.wd.setUnits(units);
            this.fdh.setUnits(units);
            this.fdd.setUnits(units);
            addToHistory(wd.getName());
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    @Override
    public WeatherData getCurrentWeather(String loc) {
        try {
            String key = getAPIKey();
            String url = "https://pro.openweathermap.org/data/2.5/weather"
                    + "?q=" + loc + "&appid=" + key
                    + "&units=" + units;
            String json = makeHTTPCall(url);
            //System.out.println(json);
            return makeWeatherObject(json);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect or missing API key.");
        }
    }

    @Override
    public WeatherData getCurrentWeather(double lat, double lon) {
        try {
            if(illegalLatOrLon(lat, lon)) {
                throw new IllegalStateException("Lat must be +/- 90, longitude +/- 180");
            }
            String key = getAPIKey();
            String latStr = String.format(Locale.US,"%.4f", lat);
            String lonStr = String.format(Locale.US,"%.4f", lon);
            String url = "https://pro.openweathermap.org/data/2.5/weather"
                    + "?lat=" + latStr + "&lon=" + lonStr + "&appid=" + key
                    + "&units=" + units;
            String json = makeHTTPCall(url);
            return makeWeatherObject(json);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect or missing API key.");
        }
    }

    @Override
    public ForecastDataHourly getForecastHourly(double lat, double lon) {
        try {
            if(illegalLatOrLon(lat, lon)) {
                throw new IllegalStateException("Lat must be +/- 90, longitude +/- 180");
            }
            String key = getAPIKey();
            String latStr = String.format(Locale.US,"%.4f", lat);
            String lonStr = String.format(Locale.US,"%.4f", lon);
            String url = "https://pro.openweathermap.org/data/2.5/forecast/hourly"
                    + "?lat=" + latStr + "&lon=" + lonStr + "&appid=" + key
                    + "&units=" + units;
            String res = makeHTTPCall(url);
            ForecastDataHourly weather = makeForecastHourlyObject(res);
            return weather;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect or missing API key.");
        }
    }

    @Override
    public ForecastDataHourly getForecastHourly(String loc) {
        Pair<Double, Double> coords = getLocation(loc);
        return getForecastHourly(coords.getKey(), coords.getValue());
    }
    
    @Override
    public ForecastDataDaily getForecastDaily(String loc) {
        try {
            Pair<Double, Double> coords = getLocation(loc);
            String key = getAPIKey();
            String url = "https://api.openweathermap.org/data/2.5/forecast/daily"
                    + "?lat=" + coords.getKey() + "&lon=" + coords.getValue()
                    + "&appid=" + key + "&units=" + units;
            String res = makeHTTPCall(url);
            ForecastDataDaily weather = makeForecastDailyObject(res);
            return weather;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect or missing API key.");
        }
    }
    
    private Pair<Double, Double> getLocation(String loc) {
        try {
            // get location from place name
            String key = getAPIKey();
            String url = "http://api.openweathermap.org/geo/1.0/direct"
                    + "?q=" + loc + "&limit=1&appid=" + key;
            String res = makeHTTPCall(url);
            //System.out.println(res);

            Gson gson = new Gson();
            LocationData[] locations = gson.fromJson(res, LocationData[].class);
            return new Pair<>(locations[0].getLat(),locations[0].getLon());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Incorrect or missing API key.");
        }
    }

    private static String getAPIKey() throws IOException {
        try {
            JsonParser parser = new JsonParser();
            FileReader reader = new FileReader("apikeys.json");
            String key = parser.parse(reader).getAsJsonObject()
                    .get("api-key").getAsString();
            reader.close();
            return key;
        } catch (IOException ex) {
            System.out.println("No apikeys.json file found.");
            return null;
        }
    }
    
    private static String makeHTTPCall(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            //System.out.println(res);
            //System.out.println(response);
            if (response.isSuccessful()) {
                return res;
            } else {
                throw new IllegalArgumentException("API key not privileged enough.");
            }
        } catch (IOException ex) {
            System.out.println("Error getting api response.");
            return null;
        }
    }
    
    private static WeatherData makeWeatherObject(String json) throws IOException {
        Gson gson = new Gson();
        WeatherData data = gson.fromJson(json, WeatherData.class);
        String aq = getAirQuality(data);
        data.setAirQuality(aq);
        return data;
    }
    
    private static ForecastDataHourly makeForecastHourlyObject(String json) {
        Gson gson = new Gson();
        ForecastDataHourly data = gson.fromJson(json, ForecastDataHourly.class);
        return data;
    }
    
    private static ForecastDataDaily makeForecastDailyObject(String json) {
        Gson gson = new Gson();
        ForecastDataDaily data = gson.fromJson(json, ForecastDataDaily.class);
        return data;
    }
    
    private static AirQuality makeAirQualityObject(String json) {
        Gson gson = new Gson();
        AirQuality aq = gson.fromJson(json, AirQuality.class);
        return aq;
    }
    
    private static String getAirQuality(WeatherData data) throws IOException {
        String lat = String.valueOf(data.getCoord().getLat());
        String lon = String.valueOf(data.getCoord().getLon());
        
        String url = "http://api.openweathermap.org/data/2.5/air_pollution"
                + "?lat=" + lat + "&lon=" + lon + "&appid=" + getAPIKey();
        String res = makeHTTPCall(url);
        //System.out.println(res);
        AirQuality aq = makeAirQualityObject(res);
        int aqi = aq.getList().get(0).getMain().getAqi();
        switch(aqi) {
            case 1: return "Good";
            case 2: return "Fair";
            case 3: return "Moderate";
            case 4: return "Poor";
            case 5: return "Very Poor";
            default: return "null";
        }
    }
    
    private static boolean illegalLatOrLon(double lat, double lon) {
        return lat < -90 || lat > 90 || lon < -180 || lon > 180;
    }
    
    private void addToHistory(String loc) {
        if(!locationHistory.contains(loc)) {
            locationHistory.add(0, loc);
        } else {
            locationHistory.remove(loc);
            addToHistory(loc);
        }
    }
}