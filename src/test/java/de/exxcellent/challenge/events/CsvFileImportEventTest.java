package de.exxcellent.challenge.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CsvFileImportEvent} test class.
 *
 * @author Lukas Jeckle
 **/
public class CsvFileImportEventTest
{

    private static CsvFileImportEvent weatherCsvFileImportEvent;
    private static CsvFileImportEvent footballCsvFileImportEvent;

    private static final String weatherFilePath  = "src/test/resources/de/exxcellent/challenge/weather.csv";
    private static final String footballFilePath = "src/test/resources/de/exxcellent/challenge/football.csv";

    @BeforeEach
    void setUp()
    {
        weatherCsvFileImportEvent  = new CsvFileImportEvent(weatherFilePath);
        footballCsvFileImportEvent = new CsvFileImportEvent(footballFilePath);
    }

    @Test
    void instantiated()
    {
        assertNotNull(weatherCsvFileImportEvent);
        assertNotNull(footballCsvFileImportEvent);
    }

    @Test
    void getFilePathWeatherCsvFileImportEvent()
    {
        assertEquals(weatherFilePath, weatherCsvFileImportEvent.getFilePath());
    }

    @Test
    void getFilePathFootballCsvFileImportEvent()
    {
        assertEquals(footballFilePath, footballCsvFileImportEvent.getFilePath());
    }

}
