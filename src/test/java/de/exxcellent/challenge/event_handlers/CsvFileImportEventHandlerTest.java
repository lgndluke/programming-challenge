package de.exxcellent.challenge.event_handlers;

import de.exxcellent.challenge.data.DataFrame;
import de.exxcellent.challenge.events.CsvFileImportEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CsvFileImportEventHandler} test class.
 *
 * @author Lukas Jeckle
 **/
public class CsvFileImportEventHandlerTest
{

    private static CsvFileImportEventHandler eventHandler;

    private static CsvFileImportEvent weatherCsvFileImportEvent;
    private static CsvFileImportEvent footballCsvFileImportEvent;

    private static CsvFileImportEvent nonExistingCsvFileImportEvent;
    private static CsvFileImportEvent nonCsvCsvFileImportEvent;

    private static final String weatherFilePath  = "src/test/resources/de/exxcellent/challenge/weather.csv";
    private static final String footballFilePath = "src/test/resources/de/exxcellent/challenge/football.csv";

    private static final String nonExistingCsvFilePath = "src/test/resources/de/exxcellent/challenge/invalid.csv";
    private static final String nonCsvFilePath         = "src/test/resources/de/exxcellent/challenge/invalid.pdf";

    private static final String emptyDataFrameToStringValue = "DataFrame: []";

    @BeforeEach
    public void setUp()
    {
        eventHandler                  = new CsvFileImportEventHandler();
        weatherCsvFileImportEvent     = new CsvFileImportEvent(weatherFilePath);
        footballCsvFileImportEvent    = new CsvFileImportEvent(footballFilePath);
        nonExistingCsvFileImportEvent = new CsvFileImportEvent(nonExistingCsvFilePath);
        nonCsvCsvFileImportEvent      = new CsvFileImportEvent(nonCsvFilePath);
    }

    @Test
    void instantiated()
    {
        assertNotNull(eventHandler);
        assertNotNull(weatherCsvFileImportEvent);
        assertNotNull(footballCsvFileImportEvent);
        assertNotNull(nonExistingCsvFileImportEvent);
        assertNotNull(nonCsvCsvFileImportEvent);
    }

    @Test
    void onEventWeatherCsvFileImport()
    {
        DataFrame dataFrame = eventHandler.onEvent(weatherCsvFileImportEvent).join();
        assertNotEquals(emptyDataFrameToStringValue, dataFrame.toString());
    }

    @Test
    void onEventFootballCsvFileImport()
    {
        DataFrame dataFrame = eventHandler.onEvent(footballCsvFileImportEvent).join();
        assertNotEquals(emptyDataFrameToStringValue, dataFrame.toString());
    }

    @Test
    void onEventNonExistingCsvFileImport()
    {
        DataFrame dataFrame = eventHandler.onEvent(nonExistingCsvFileImportEvent).join();
        assertEquals(emptyDataFrameToStringValue, dataFrame.toString());
    }

    @Test
    void onEventNonCsvCsvFileImport()
    {
        DataFrame dataFrame = eventHandler.onEvent(nonCsvCsvFileImportEvent).join();
        assertEquals(emptyDataFrameToStringValue, dataFrame.toString());
    }

}
