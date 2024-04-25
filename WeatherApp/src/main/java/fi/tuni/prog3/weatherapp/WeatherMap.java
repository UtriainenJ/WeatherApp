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
 * Class used for handling map API calls, zooming, and moving.
 * @author Jerri Tarpio
 */
public class WeatherMap {
    private static String API_KEY;
    private static final String FILE_NAME = "map.png";
    private static final int MAX_ZOOM = 3;
    private static final int MIN_ZOOM = 0;
    private static final int MIN_COORD = 0;
    private static int MAX_COORD;
    
    private static int ZOOM;
    private static int X;
    private static int Y;
    private static String MODE;

    /**
     * Call init() always before using other methods
     */
    public static void init() {
        WeatherMap.ZOOM = 0;
        WeatherMap.X = 0;
        WeatherMap.Y = 0;
        WeatherMap.MAX_COORD = 0;
        WeatherMap.MODE = "PR0";
        WeatherMap.API_KEY = getAPIKey();
        getImg();
    }
    
    /**
     * Check if able to zoom in more
     * @return boolean true if able to
     */
    public static boolean canZoomIn() {
        return ZOOM != MAX_ZOOM;
    }
    
    /**
     * Check if able to zoom out more
     * @return boolean true if able to
     */
    public static boolean canZoomOut() {
        return ZOOM != MIN_ZOOM;
    }
    
    /**
     * Zoom into map
     * @return true if new API image call worked
     */
    public static boolean zoomIn() {
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
    
    /**
     * Zoom out map
     * @return boolean true if new API image call worked
     */
    public static boolean zoomOut() {
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
    
    /**
     * Move map upwards
     * @return boolean true if new API image call worked
     */
    public static boolean moveUp() {
        if(Y-1 < MIN_COORD) {
            Y = MAX_COORD;
        } else {
            Y--;
        }
        return getImg();
    }

    /**
     * Move map downwards
     * @return boolean true if new API image call worked
     */
    public static boolean moveDown() {
        if(Y+1 > MAX_COORD) {
            Y = MIN_COORD;
        } else {
            Y++;
        }
        return getImg();
    }
    
    /**
     * Move map right
     * @return boolean true if new API image call worked
     */
    public static boolean moveRight() {
        if(X+1 > MAX_COORD) {
            X = MIN_COORD;
        } else {
            X++;
        }
        return getImg();
    }
    
    /**
     * Move map left
     * @return boolean true if new API image call worked
     */
    public static boolean moveLeft() {
        if(X-1 < MIN_COORD) {
            X = MAX_COORD;
        } else {
            X--;
        }
        return getImg();
    }
    
    /**
     * Set map mode: (Rain, Temperature, Wind, Clouds)
     * @param mode
     * @return boolean true if new API image call worked
     */
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
    
    /**
     * Get filename of base map that is stored locally
     * @return String base map filename
     */
    public static String getBaseFilename() {
        return String.format("map/%d%d%d.png", ZOOM, X, Y);
    }
    
    /**
     * Get filename where API weather maps are saved
     * @return String weather map filename
     */
    public static String getFileName() {
        return FILE_NAME;
    }
    
    private static int getMaxCoord(int z) {
        return (int) Math.pow(2, z) - 1;
    }
    
    private static boolean getImg() {
        String url = "https://maps.openweathermap.org/maps/2.0/weather/"
                + MODE + "/" + ZOOM + "/" + X + "/" + Y + "?fill_bound=true"
                + "&appid=" + API_KEY;
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
            byte[] imageWeather = rb.bytes();
            
            FileOutputStream fos = new FileOutputStream(FILE_NAME);
            fos.write(imageWeather);
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

