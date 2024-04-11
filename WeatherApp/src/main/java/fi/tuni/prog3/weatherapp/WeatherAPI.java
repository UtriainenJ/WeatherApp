package fi.tuni.prog3.weatherapp;

import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author jerri
 */
public class WeatherAPI implements iAPI {

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
            return "00000000000000000000000000000000";
        }
    }
    
    private static String makeHTTPCall(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            return response.isSuccessful() ? response.body().string() : null;
        } catch (IOException ex) {
            System.out.println("Error getting api response.");
            return null;
        }
    }

    @Override
    public String lookUpLocation(String loc) {
        try {
            String key = getAPIKey();
            String url = "https://api.openweathermap.org/data/2.5/weather"
                    + "?q=" + loc + "&appid=" + key;
            return makeHTTPCall(url);
        } catch (IOException ex) {return null;}
    }

    @Override
    public String getCurrentWeather(double lat, double lon) {
        try {
            String key = getAPIKey();
            String latStr = String.format(Locale.US,"%.2f", lat);
            String lonStr = String.format(Locale.US,"%.2f", lon);
            String url = "https://api.openweathermap.org/data/2.5/weather"
                    + "?lat=" + latStr + "&lon=" + lonStr + "&appid=" + key;
            return makeHTTPCall(url);
        } catch (IOException ex) {return null;}
    }

    @Override
    public String getForecast(double lat, double lon) {
        try {
            String key = getAPIKey();
            String latStr = String.format(Locale.US,"%.2f", lat);
            String lonStr = String.format(Locale.US,"%.2f", lon);
            String url = "https://api.openweathermap.org/data/2.5/forecast"
                    + "?lat=" + latStr + "&lon=" + lonStr + "&appid=" + key;
            return makeHTTPCall(url);
        } catch (IOException ex) {return null;}
    }
}