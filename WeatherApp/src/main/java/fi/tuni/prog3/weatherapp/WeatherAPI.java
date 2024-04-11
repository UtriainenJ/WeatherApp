package fi.tuni.prog3.weatherapp;

import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
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

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            
            try {
                Response response = client.newCall(request).execute();

                // Check if the response is successful
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException ex) {
                System.out.println("Error getting api response.");
            }
        return null;
    }

    @Override
    public String lookUpLocation(String loc) {
        try {
            //https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key};

            String key = getAPIKey();
            String city = loc;
            
            String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key;
            
            String res = makeHTTPCall(url);
            return res;
            
        } catch (IOException ex) {
            // api file not found failure
        }
        
        return null;
    }

    @Override
    public String getCurrentWeather(double lat, double lon) {
        //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
        
        return null;
    }

    @Override
    public String getForecast(double lat, double lon) {
        //https://pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
        
        return null;
    }
    
}
