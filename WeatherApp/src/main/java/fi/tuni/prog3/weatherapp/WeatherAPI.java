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
 * A WeatherAPI object is used to get weather info on cities.
 * Preferred method of getting weather info is with getData(*cityname*).
 * Cities looked up with getData(*cityname*) get saved in browsing history,
 * and the last one looked up gets saved as the active city.
 * 
 * Browsing history can be deleted.
 * You can add and remove cities from favourites.
 * 
 * Interpreting the given results:
 * 
 * WeatherData object data corresponds to this link's fields:
 * https://openweathermap.org/current
 * 
 * Same with ForecastData:
 * https://openweathermap.org/forecast5
 * 
 * with getData() pair you can use .getKey() to access the WeatherData object,
 * and .getValue() to access the ForecastData object.
 * 
 * 
 *  **Use example:**
 * 
 *  **loading WeatherAPI from file**
 * 
 * StorageSystem ss = new StorageSystem("temp.json");
 * WeatherAPI api = ss.readFromFile();
 * 
 *  **getting location info by city name, this saves the city to history,**
 *  **and sets the most recent one as active.**
 * 
 * Pair<WeatherData, ForecastData> nyc = api.getData("New York City");
 * var hervanta = api.getData("Hervanta");
 * 
 * **calling getCurrentWeather() or getForecast() does not do saving to history.**
 * **you can use coordinates or city name.**
 * 
 * WeatherData vaasaW = api.getCurrentWeather(63.096, 21.61577);
 * WeatherData osloW = api.getCurrentWeather("Oslo");
 * 
 * ForecastData tampereF = api.getForecast("Tampere");
 * ForecastData toijalaF = api.getForecast(61.166666, 23.86749653);
 * 
 *  **deleting browsing history:**
 * api.clearBrowsingHistory();
 * 
 *  **adding and removing from favorites:**
 * api.addToFavorites(nyc.getKey().getName());
 * api.removeFromFavorites(hervanta.getKey().getName());
 */

/**
 *
 * @author jerri
 */
public class WeatherAPI implements iAPI {
    
    private String locationActive;
    private List<String> locationFavorites;
    private List<String> locationHistory;

    public String getLocationActive() {
        return locationActive;
    }

    public List<String> getLocationFavorites() {
        return locationFavorites;
    }

    public List<String> getLocationHistory() {
        return locationHistory;
    }

    public WeatherAPI() throws Exception {
        this.locationActive = null;
        this.locationFavorites = new ArrayList<>();
        this.locationHistory = new ArrayList<>();
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
        
    public Pair<WeatherData, ForecastData> getData(String loc) {
        WeatherData wd = getCurrentWeather(loc);
        ForecastData fd = getForecast(loc);
        Pair<WeatherData, ForecastData> WeatherEntry = new Pair<>(wd, fd);
        locationActive = loc;
        addToHistory(loc);
        return WeatherEntry;
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
    
    private void addToHistory(String loc) {
        if(!locationHistory.contains(loc)) {
            locationHistory.add(loc);
        }
    }
}