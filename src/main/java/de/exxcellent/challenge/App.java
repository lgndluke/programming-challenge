package de.exxcellent.challenge;

import de.exxcellent.challenge.data.DataFrame;
import de.exxcellent.challenge.event_handlers.CsvFileImportEventHandler;
import de.exxcellent.challenge.event_handlers.MinDistanceProcessingEventHandler;
import de.exxcellent.challenge.events.CsvFileImportEvent;
import de.exxcellent.challenge.events.MinDistanceProcessingEvent;
import de.exxcellent.challenge.framework.EventDispatcher;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 */
public class App {

    private final static String weatherFilePath  = "src/main/resources/de/exxcellent/challenge/weather.csv";
    private final static String footballFilePath = "src/main/resources/de/exxcellent/challenge/football.csv";

    /**
     * This is the main entry method of your program.
     * @param args The CLI arguments passed
     */
    public static void main(String... args)
    {
        EventDispatcher.getInstance().registerHandler(
                CsvFileImportEvent.class,
                new CsvFileImportEventHandler()
        );
        EventDispatcher.getInstance().registerHandler(
                MinDistanceProcessingEvent.class,
                new MinDistanceProcessingEventHandler()
        );

        CsvFileImportEvent weatherImportEvent  = new CsvFileImportEvent(weatherFilePath);
        CsvFileImportEvent footballImportEvent = new CsvFileImportEvent(footballFilePath);

        DataFrame weatherDataFrame  = (DataFrame) EventDispatcher.getInstance().dispatchEvent(weatherImportEvent).join();
        DataFrame footballDataFrame = (DataFrame) EventDispatcher.getInstance().dispatchEvent(footballImportEvent).join();

        MinDistanceProcessingEvent smallestTempSpread = new MinDistanceProcessingEvent(
                weatherDataFrame.getColumnValues("MnT"),
                weatherDataFrame.getColumnValues("MxT"),
                weatherDataFrame.getColumnValues("Day")
        );
        MinDistanceProcessingEvent smallestGoalSpread = new MinDistanceProcessingEvent(
                footballDataFrame.getColumnValues("Goals"),
                footballDataFrame.getColumnValues("Goals Allowed"),
                footballDataFrame.getColumnValues("Team")
        );

        String dayWithSmallestTempSpread  = (String) EventDispatcher.getInstance().dispatchEvent(smallestTempSpread).join();
        String teamWithSmallestGoalSpread = (String) EventDispatcher.getInstance().dispatchEvent(smallestGoalSpread).join();

        System.out.printf("Day with smallest temperature spread : %s%n", dayWithSmallestTempSpread);
        System.out.printf("Team with smallest goal spread       : %s%n", teamWithSmallestGoalSpread);
    }

}
