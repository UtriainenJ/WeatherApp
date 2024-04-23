package fi.tuni.prog3.weatherapp;

import com.google.gson.JsonParser;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * @author jerri
 */
public class WeatherMap {
    private static final String FILE_NAME = "map.png";
    private static final int MAX_ZOOM = 4;
    private static final int MIN_ZOOM = 0;
    private static final int MIN_COORD = 0;
    private static int MAX_COORD;
    
    private static int ZOOM;
    private static int X;
    private static int Y;
    private static String MODE;

    public static void init() {
        WeatherMap.ZOOM = 0;
        WeatherMap.X = 0;
        WeatherMap.Y = 0;
        WeatherMap.MAX_COORD = 0;
        WeatherMap.MODE = "PR0";
        getImg();
    }
    public static boolean zoom_in() {
        if(ZOOM >= MAX_ZOOM) {
            return false;
        }
        ZOOM++;
        //update x and y
        X = X*2;
        Y = Y*2;
        MAX_COORD = getMaxCoord(ZOOM);
        return getImg();
    }
    
    public static boolean zoom_out() {
        if(ZOOM <= MIN_ZOOM) {
            return false;
        } 
        ZOOM--;
        //update x and y
        X = X/2;
        Y = Y/2;
        MAX_COORD = getMaxCoord(ZOOM);
        return getImg();
    }
    
    public static boolean move_y(int num) {
        if(Y+num < MIN_COORD || Y+num > MAX_COORD) {
            return false;
        }
        Y += num;
        return getImg();
    }
    
    public static boolean move_x(int num) {
        if(X+num < MIN_COORD || X+num > MAX_COORD) {
            return false;
        }
        X += num;
        return getImg();
    }
    
    public static boolean setMode(String mode) {
        switch(mode) {
            case "Rain":
                MODE = "PR0"; return getImg();
            case "Temperature":
                MODE = "TA2"; return getImg();
            case "Wind":
                MODE = "WND"; return getImg();
            case "Clouds":
                MODE = "CL"; return getImg();
            default:
                return false;
        }
    }
    
    private static int getMaxCoord(int z) {
        if(z <= 1) return z;
        return z*z-1;
    }
    
    private static boolean getImg() {
        String key = getAPIKey();
        String url = "https://maps.openweathermap.org/maps/2.0/weather/"
                + MODE + "/" + ZOOM + "/" + X + "/" + Y + "?fill_bound=true&opacity=0.7"
                + "&appid=" + key;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()){
            if(!response.isSuccessful()) {
                throw new IOException("Bad response");
            }
            
            ResponseBody responseBody = response.body();
            if(responseBody != null) {
                return saveImgToFile(responseBody);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return false;
    }

    private static boolean saveImgToFile(ResponseBody rb) {
        try {
            byte[] image = rb.bytes();
        
            FileOutputStream fos = new FileOutputStream(FILE_NAME);
            fos.write(image);
            fos.close();
            return true;
        } catch (IOException ex) {
        return false;}
    }

    private static String getAPIKey() {
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
}

