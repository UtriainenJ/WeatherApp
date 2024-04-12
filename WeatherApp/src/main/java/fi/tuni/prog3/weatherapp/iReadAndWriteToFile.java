package fi.tuni.prog3.weatherapp;

import java.io.IOException;

/**
 * Interface with methods to read from a file and write to a file.
 */
public interface iReadAndWriteToFile {

    /**
     * Reads JSON from the given file.
     * @param fileName name of the file to read from.
     * @return true if the read was successful, otherwise false.
     * @throws IOException if the method e.g, cannot find the file.
     */
    public String readFromFile(String fileName) throws IOException;

    /**
     * Write the student progress as JSON into the given file.
     * @param api WeatherAPI object
     * @return true if the write was successful, otherwise false.
     * @throws IOException if the method e.g., cannot write to a file.
     */
    public boolean writeToFile(WeatherAPI api) throws IOException;
}
