package fi.tuni.prog3.weatherapp;

import java.io.IOException;

/**
 * Interface with methods to read from a file and write to a file.
 */
public interface iReadAndWriteToFile {

    /**
     * Reads JSON from file
     * @return WeatherAPI object read from file
     * @throws IOException if the method e.g, cannot find the file.
     */
    public WeatherAPI readFromFile() throws IOException;

    /**
     * Write the WeatherAPI data to file
     * @param api WeatherAPI object
     * @return true if the write was successful, otherwise false.
     * @throws IOException if the method e.g., cannot write to a file.
     */
    public boolean writeToFile(WeatherAPI api) throws IOException;
}
