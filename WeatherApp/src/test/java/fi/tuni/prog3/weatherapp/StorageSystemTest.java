package fi.tuni.prog3.weatherapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Tests StorageSystem by creating a temporary file, writing to it, and reading from it
 * @author Jaakko
 */

public class StorageSystemTest {
    private StorageSystem storage;
    private String filename = "testFile.json";

    /**
     * Setup to make tests work
     */
    @Before
    public void setUp() throws IOException {
        // Set up a test JSON file
        FileWriter writer = new FileWriter(filename);
        writer.write("{\"locationActive\":\"Raisio\",\"units\":\"m\"}");
        writer.close();

        storage = new StorageSystem(filename);
    }

    /**
     * Deletes the temporary file
     */
    @After
    public void tearDown() {
        // Clean up the test file
        new File(filename).delete();
    }


    /**
     * Tests readFromFile
     */
    @Test
    public void testReadFromFile() throws IOException {
        // Execute
        WeatherAPI result = storage.readFromFile();

        // Validate the results
        assertNotNull("The read result should not be null", result);
        assertEquals("The location should be 'Raisio'", "Raisio", result.getLocationActive());
    }

    /**
     * Tests WriteToFile
     */
    @Test
    public void testWriteToFile() throws Exception {
        WeatherAPI api = new WeatherAPI();
        api.setLocationActive("Raisio");

        boolean result = storage.writeToFile(api);

        assertTrue("File should have been written successfully", result);

        // Read back the file content to verify it was written correctly
        String content = Files.lines(Paths.get(filename)).collect(Collectors.joining());
        Gson gson = new Gson();
        WeatherAPI readApi = gson.fromJson(content, WeatherAPI.class);

        assertNotNull(readApi);
        assertEquals("Location should match", "Raisio", readApi.getLocationActive());
    }

}
