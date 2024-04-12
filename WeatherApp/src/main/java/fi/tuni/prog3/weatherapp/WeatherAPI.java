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
 *
 * @author jerri
 */
public class WeatherAPI implements iAPI {
    
    private String locationActive;
    private List<String> locationFavorites;
    private List<String> locationHistory;

    public WeatherAPI() {
        this.locationActive = null;
        this.locationFavorites = new ArrayList<>();
        this.locationHistory = new ArrayList<>();
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
            //System.out.println(response);
            if (response.isSuccessful()) {
                String res = response.body().string();
                return res;
            } else {
                return null;
            }
        } catch (IOException ex) {
            System.out.println("Error getting api response.");
            return null;
        }
    }
    
    private static WeatherData makeWeatherObject(String json) {
        Gson gson = new Gson();
        WeatherData data = gson.fromJson(json, WeatherData.class);
        return data;
    }
    
    private static ForecastData makeForecastObject(String json) {
        Gson gson = new Gson();
        ForecastData data = gson.fromJson(json, ForecastData.class);
        return data;
    }
    
    private static boolean illegalLatOrLon(double lat, double lon) {
        return lat < -90 || lat > 90 || lon < -90 || lon > 90;
    }
    
    public void addToFavorites(String loc) {
        if(!locationFavorites.contains(loc)) {
            locationFavorites.add(loc);
        }
    }
    
    public void removeFromFavorites(String loc) {
        if(locationFavorites.contains(loc)) {
            locationFavorites.remove(loc);
        }
    }
    
    public void clearBrowsingHistory() {
        this.locationHistory.clear();
    }

    @Override
    public WeatherData getCurrentWeather(String loc) {
        try {
            String key = getAPIKey();
            String url = "https://api.openweathermap.org/data/2.5/weather"
                    + "?q=" + loc + "&appid=" + key;
            String json = makeHTTPCall(url);
            return makeWeatherObject(json);
        } catch (IOException ex) {return null;}
    }

    @Override
    public WeatherData getCurrentWeather(double lat, double lon) {
        try {
            if(illegalLatOrLon(lat, lon)) {
                throw new IllegalStateException("Lat and lon must be +/- 90");
            }
            String key = getAPIKey();
            String latStr = String.format(Locale.US,"%.4f", lat);
            String lonStr = String.format(Locale.US,"%.4f", lon);
            String url = "https://api.openweathermap.org/data/2.5/weather"
                    + "?lat=" + latStr + "&lon=" + lonStr + "&appid=" + key;
            String json = makeHTTPCall(url);
            return makeWeatherObject(json);
        } catch (IOException ex) {return null;}
    }

    @Override
    public ForecastData getForecast(double lat, double lon) {
        try {
            if(illegalLatOrLon(lat, lon)) {
                throw new IllegalStateException("Lat and lon must be +/- 90");
            }
            String key = getAPIKey();
            String latStr = String.format(Locale.US,"%.4f", lat);
            String lonStr = String.format(Locale.US,"%.4f", lon);
            String url = "https://api.openweathermap.org/data/2.5/forecast"
                    + "?lat=" + latStr + "&lon=" + lonStr + "&appid=" + key;
            String res = makeHTTPCall(url);
            ForecastData wd = makeForecastObject(res);
            return wd;
        } catch (IOException ex) {return null;}
    }

    @Override
    public ForecastData getForecast(String loc) {
        try {
            String key = getAPIKey();
            String url = "http://api.openweathermap.org/geo/1.0/direct"
                    + "?q=" + loc + "&limit=1&appid=" + key;
            String res = makeHTTPCall(url);
            //System.out.println(res);
            
            Gson gson = new Gson();
            LocationData[] locations = gson.fromJson(res, LocationData[].class);

            return getForecast(locations[0].getLat(), locations[0].getLon());
            
        } catch (IOException ex) {return null;}
    }
    
    public Pair<WeatherData, ForecastData> getData(String loc) {
        WeatherData wd = getCurrentWeather(loc);
        ForecastData fd = getForecast(loc);
        Pair<WeatherData, ForecastData> WeatherEntry = new Pair<>(wd, fd);
        locationActive = loc;
        locationHistory.add(loc);
        return WeatherEntry;
    }
    
    public boolean writeToFile() throws Exception {
        return new StorageSystem().writeToFile(this);
    }
}