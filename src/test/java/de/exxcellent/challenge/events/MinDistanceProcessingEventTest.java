package de.exxcellent.challenge.events;

import de.exxcellent.challenge.data.DataFrame;
import de.exxcellent.challenge.event_handlers.CsvFileImportEventHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link MinDistanceProcessingEvent} test class.
 *
 * @author Lukas Jeckle
 **/
public class MinDistanceProcessingEventTest
{

    private static MinDistanceProcessingEvent smallestTempSpread;
    private static MinDistanceProcessingEvent smallestGoalSpread;

    private static final String weatherFilePath  = "src/test/resources/de/exxcellent/challenge/weather.csv";
    private static final String footballFilePath = "src/test/resources/de/exxcellent/challenge/football.csv";

    private static DataFrame weatherDataFrame;
    private static DataFrame footballDataFrame;

    @BeforeAll
    static void setupAll()
    {
        CsvFileImportEventHandler csvFileImportEventHandler = new CsvFileImportEventHandler();

        CsvFileImportEvent weatherImportEvent  = new CsvFileImportEvent(weatherFilePath);
        CsvFileImportEvent footballImportEvent = new CsvFileImportEvent(footballFilePath);

        weatherDataFrame  = csvFileImportEventHandler.onEvent(weatherImportEvent).join();
        footballDataFrame = csvFileImportEventHandler.onEvent(footballImportEvent).join();
    }

    @BeforeEach
    void setUpEach()
    {
        smallestTempSpread = new MinDistanceProcessingEvent(
                weatherDataFrame.getColumnValues("MnT"),
                weatherDataFrame.getColumnValues("MxT"),
                weatherDataFrame.getColumnValues("Day")
        );

        smallestGoalSpread = new MinDistanceProcessingEvent(
                footballDataFrame.getColumnValues("Goals"),
                footballDataFrame.getColumnValues("Goals Allowed"),
                footballDataFrame.getColumnValues("Team")
        );
    }

    @Test
    void instantiated()
    {
        assertNotNull(smallestTempSpread);
        assertNotNull(smallestGoalSpread);
    }

    @Test
    void getMinValuesSmallestTempSpread()
    {
        List<String> minValues = smallestTempSpread.getMinValues();
        assertEquals(weatherDataFrame.getColumnValues("MnT"), minValues);
    }

    @Test
    void getMinValuesSmallestGoalSpread()
    {
        List<String> minValues = smallestGoalSpread.getMinValues();
        assertEquals(footballDataFrame.getColumnValues("Goals"), minValues);
    }

    @Test
    void getMaxValuesSmallestTempSpread()
    {
        List<String> maxValues = smallestTempSpread.getMaxValues();
        assertEquals(weatherDataFrame.getColumnValues("MxT"), maxValues);
    }

    @Test
    void getMaxValuesSmallestGoalSpread()
    {
        List<String> maxValues = smallestGoalSpread.getMaxValues();
        assertEquals(footballDataFrame.getColumnValues("Goals Allowed"), maxValues);
    }

    @Test
    void getReturnValuesSmallestTempSpread()
    {
        List<String> returnValues = smallestTempSpread.getReturnValues();
        assertEquals(weatherDataFrame.getColumnValues("Day"), returnValues);
    }

    @Test
    void getReturnValuesSmallestGoalSpread()
    {
        List<String> returnValues = smallestGoalSpread.getReturnValues();
        assertEquals(footballDataFrame.getColumnValues("Team"), returnValues);
    }

}
