package fi.tuni.prog3.weatherapp;

import java.io.IOException;

/**
 * Interface with methods to read from a file and write to a file.
 */
public interface iReadAndWriteToFile {

    /**
     * Reads JSON from file
     * @return WeatherAPI object read from file
     */
    public WeatherAPI readFromFile();

    /**
     * Write the WeatherAPI data to file
     * @param api WeatherAPI object
     * @return true if the write was successful, otherwise false.
     */
    public boolean writeToFile(WeatherAPI api);
}
