package fi.tuni.prog3.weatherapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Jerri Tarpio
 * Used for storing and loading WeatherAPI to/from drive.
 */
public class StorageSystem implements iReadAndWriteToFile {
        
    private final String filename;

    /**
     * Constructor
     * @param filename
     */
    public StorageSystem(String filename) {
        this.filename = filename;
    }

    /**
     * Reads WeatherAPI attributes from file
     * @return WeatherAPI object loaded from drive. If not found, blank object.
     */
    @Override
    public WeatherAPI readFromFile() {
        Gson gson = new Gson();
        try (FileReader rd = new FileReader(filename)) {
            WeatherAPI api = gson.fromJson(rd, WeatherAPI.class);
            return api;
        } catch (IOException ex) {
            try {
                //create new file if no file found
                WeatherAPI api = new WeatherAPI();
                writeToFile(api);
                return readFromFile();
            } catch (Exception ex1) {
                return null;
            }
        }
    }

    /**
     * Save WeatherAPI object attributes to file
     * @param api WeatherAPI object to write to file
     * @return boolean if write was successful
     */
    @Override
    public boolean writeToFile(WeatherAPI api) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                .create();
        try (FileWriter wr = new FileWriter(filename)) {
            gson.toJson(api, wr);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
