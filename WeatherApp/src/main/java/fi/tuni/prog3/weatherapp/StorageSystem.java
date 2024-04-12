package fi.tuni.prog3.weatherapp;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author jerri
 */
public class StorageSystem implements iReadAndWriteToFile {
        
    private final String filename;

    public StorageSystem(String filename) {
        this.filename = filename;
    }

    @Override
    public WeatherAPI readFromFile() throws IOException {
        Gson gson = new Gson();
        try (FileReader rd = new FileReader(filename)) {
            WeatherAPI api = gson.fromJson(rd, WeatherAPI.class);
            return api;
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public boolean writeToFile(WeatherAPI api) throws IOException {
        Gson gson = new Gson();
        try (FileWriter wr = new FileWriter(filename)) {
            gson.toJson(api, wr);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
