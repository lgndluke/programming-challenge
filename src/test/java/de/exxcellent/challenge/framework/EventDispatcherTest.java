package de.exxcellent.challenge.framework;

import de.exxcellent.challenge.data.DataFrame;
import de.exxcellent.challenge.event_handlers.CsvFileImportEventHandler;
import de.exxcellent.challenge.event_handlers.MinDistanceProcessingEventHandler;
import de.exxcellent.challenge.events.CsvFileImportEvent;
import de.exxcellent.challenge.events.MinDistanceProcessingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link EventDispatcher} test class.
 *
 * @author Lukas Jeckle
 **/
public class EventDispatcherTest
{

    private static final String weatherFilePath  = "src/test/resources/de/exxcellent/challenge/weather.csv";

    @BeforeEach
    void setUp()
    {
        EventDispatcher.getInstance().registerHandler(
                CsvFileImportEvent.class,
                new CsvFileImportEventHandler()
        );
    }

    @Test
    void instantiated()
    {
        assertNotNull(EventDispatcher.getInstance());
    }

    @Test
    void registerHandlerDoubledCsvFileImportEvent()
    {
        assertDoesNotThrow(() -> EventDispatcher.getInstance().registerHandler(
                CsvFileImportEvent.class,
                new CsvFileImportEventHandler())
        );
    }

    @Test
    void registerHandlerMinDistanceProcessingEvent()
    {
        assertDoesNotThrow(() -> EventDispatcher.getInstance().registerHandler(
                MinDistanceProcessingEvent.class,
                new MinDistanceProcessingEventHandler()
        ));
    }

    @Test
    void dispatchEventWeatherCsvFileImportEvent()
    {
        CsvFileImportEvent weatherFileImportEvent = new CsvFileImportEvent(weatherFilePath);
        DataFrame weatherDataFrame = (DataFrame) EventDispatcher.getInstance().dispatchEvent(weatherFileImportEvent).join();

        assertNotNull(weatherDataFrame);
    }

    @Test
    void dispatchEventWithUnregisteredHandler()
    {
        MinDistanceProcessingEvent smallestTempSpread = new MinDistanceProcessingEvent(
                List.of("1, 2, 3"),
                List.of("10, 9, 8"),
                List.of("a, b, c")
        );

        String minDistance = (String) EventDispatcher.getInstance().dispatchEvent(smallestTempSpread).join();

        assertNull(minDistance);
    }

}
