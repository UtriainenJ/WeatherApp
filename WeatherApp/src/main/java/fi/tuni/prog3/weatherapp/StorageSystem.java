package fi.tuni.prog3.weatherapp;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author jerri
 */
public class StorageSystem implements iReadAndWriteToFile {

    @Override
    public String readFromFile(String fileName) throws IOException {
        return "not implemented";
    }

    @Override
    public boolean writeToFile(WeatherAPI api) throws IOException {
        Gson gson = new Gson();
        try (FileWriter wr = new FileWriter("temp.json")) {
            gson.toJson(api, wr);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
