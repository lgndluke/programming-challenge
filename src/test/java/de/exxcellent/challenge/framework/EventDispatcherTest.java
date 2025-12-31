package de.exxcellent.challenge.framework;

import de.exxcellent.challenge.data.DataFrame;
import de.exxcellent.challenge.event_handlers.CsvFileImportEventHandler;
import de.exxcellent.challenge.event_handlers.MinDistanceProcessingEventHandler;
import de.exxcellent.challenge.events.CsvFileImportEvent;
import de.exxcellent.challenge.events.MinDistanceProcessingEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

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
    void getInstanceReturnsSameInstanceOnDifferentThreads() throws ExecutionException, InterruptedException
    {
        Future<EventDispatcher> future1 = CompletableFuture.supplyAsync(EventDispatcher::getInstance);
        Future<EventDispatcher> future2 = CompletableFuture.supplyAsync(EventDispatcher::getInstance);

        EventDispatcher instance1 = future1.get();
        EventDispatcher instance2 = future2.get();

        System.out.println("Instance 1: " + instance1);
        System.out.println("Instance 2: " + instance2);
        assertSame(instance1, instance2);
    }

    @Test
    void getInstanceIsThreadSafe() throws InterruptedException
    {
        int numberOfThreads  = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch            = new CountDownLatch(numberOfThreads);

        AtomicReference<EventDispatcher> instance = new AtomicReference<>();
        instance.set(EventDispatcher.getInstance());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++)
        {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
            {
                EventDispatcher dispatcher = EventDispatcher.getInstance();
                assertSame(instance.get(), dispatcher);
                latch.countDown();
            }, executorService);

            futures.add(future);
        }

        latch.await();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();
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
