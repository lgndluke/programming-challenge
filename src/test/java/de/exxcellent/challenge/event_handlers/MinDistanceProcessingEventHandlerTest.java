package de.exxcellent.challenge.event_handlers;

import de.exxcellent.challenge.data.DataFrame;
import de.exxcellent.challenge.events.CsvFileImportEvent;
import de.exxcellent.challenge.events.MinDistanceProcessingEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link MinDistanceProcessingEventHandler} test class.
 *
 * @author Lukas Jeckle
 **/
public class MinDistanceProcessingEventHandlerTest
{

    private static MinDistanceProcessingEventHandler eventHandler;

    private static MinDistanceProcessingEvent smallestTempSpread;
    private static MinDistanceProcessingEvent smallestGoalSpread;
    private static MinDistanceProcessingEvent minMaxTwistedSpread;
    private static MinDistanceProcessingEvent differentListSizesSpread;

    private static final String weatherFilePath  = "src/test/resources/de/exxcellent/challenge/weather.csv";
    private static final String footballFilePath = "src/test/resources/de/exxcellent/challenge/football.csv";

    private static DataFrame weatherDataFrame;
    private static DataFrame footballDataFrame;

    @BeforeAll
    static void setUpAll()
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
        eventHandler = new MinDistanceProcessingEventHandler();

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

        minMaxTwistedSpread = new MinDistanceProcessingEvent(
                weatherDataFrame.getColumnValues("MxT"),
                weatherDataFrame.getColumnValues("MnT"),
                weatherDataFrame.getColumnValues("Day")
        );

        differentListSizesSpread = new MinDistanceProcessingEvent(
                footballDataFrame.getColumnValues("Goals"),
                weatherDataFrame.getColumnValues("MxT"),
                weatherDataFrame.getColumnValues("Day")
        );
    }

    @Test
    void instantiated()
    {
        assertNotNull(eventHandler);
        assertNotNull(smallestTempSpread);
        assertNotNull(smallestGoalSpread);
        assertNotNull(minMaxTwistedSpread);
        assertNotNull(differentListSizesSpread);
    }

    @Test
    void onEventSmallestTempSpread()
    {
        String minDistance = eventHandler.onEvent(smallestTempSpread).join();
        assertNotNull(minDistance);
    }

    @Test
    void onEventFootballSpread()
    {
        String minDistance = eventHandler.onEvent(smallestGoalSpread).join();
        assertNotNull(minDistance);
    }

    @Test
    void onEventMinMaxTwistedSpread()
    {
        String minDistance        = eventHandler.onEvent(smallestTempSpread).join();
        String minDistanceTwisted = eventHandler.onEvent(minMaxTwistedSpread).join();

        assertEquals(minDistance, minDistanceTwisted);
    }

    @Test
    void onEventDifferentListSizesSpread()
    {
        String minDistance = eventHandler.onEvent(differentListSizesSpread).join();
        assertNull(minDistance);
    }

}
