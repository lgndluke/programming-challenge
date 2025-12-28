package de.exxcellent.challenge.event_handlers;

import de.exxcellent.challenge.events.MinDistanceProcessingEvent;
import de.exxcellent.challenge.framework.EventDispatcher;
import de.exxcellent.challenge.framework.EventHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Event handler for {@link MinDistanceProcessingEvent}. <br>
 * <br>
 * Handles {@link MinDistanceProcessingEvent}s by asynchronously processing the absolute distance between each pair
 * of {@code minValues} and {@code maxValues}. Returns the value of {@code returnValues} at the row-index with the
 * minimum absolute distance.
 *
 * @author Lukas Jeckle
 **/
public class MinDistanceProcessingEventHandler implements EventHandler<MinDistanceProcessingEvent>
{

    /**
     * Invoked by the {@link EventDispatcher} whenever a {@link MinDistanceProcessingEvent} is fired. <br>
     * Prerequisites: A {@link MinDistanceProcessingEventHandler} has been registered with the {@link EventDispatcher}. <br>
     *
     * @param event The {@link MinDistanceProcessingEvent} to be handled.
     * @return      A {@link CompletableFuture} completing with a {@link String} object that either contains the {@code
     *              returnValues} lists content from the row-index with the minimum absolute distance, or {@code null}
     *              if an error had occurred.
     **/
    @Override
    public CompletableFuture<String> onEvent(MinDistanceProcessingEvent event)
    {
        return CompletableFuture.supplyAsync(() ->
        {
            try
            {
                return process(
                        event.getMinValues(),
                        event.getMaxValues(),
                        event.getReturnValues()
                );
            }
            catch (IllegalArgumentException exception)
            {
                System.out.println(exception.getMessage());
                return null;
            }
        });
    }

    /**
     * Private method to process the {@link MinDistanceProcessingEvent}. <br>
     *
     * Validation checks performed:
     * <ul>
     *     <li>Provided lists are all of equal size.</li>
     *     <li>Provided list {@code minValues} is parseable to {@link List} of {@link Integer}.</li>
     *     <li>Provided list {@code maxValues} is parseable to {@link List} of {@link Integer}.</li>
     * </ul>
     *
     * @param minValues A {@link List} of {@link String} representing the minimum values for the spread calculation.
     * @param maxValues A {@link List} of {@link String} representations the maximum values for the spread calculation.
     * @param returnValues A {@link List} of {@link String} representations that serve as return keys.
     *
     * @return The {@code returnValues} lists value at the row-index with the minimum absolute distance.
     * @throws IllegalArgumentException if any validation rule is violated.
     **/
    private String process(List<String> minValues, List<String> maxValues, List<String> returnValues) throws IllegalArgumentException
    {
        if (!(minValues.size() == maxValues.size() && maxValues.size() == returnValues.size()))
        {
            throw new IllegalArgumentException(
                    "MinDistanceProcessingEventHandler::process: Provided lists are not of the same size!"
            );
        }

        List<Integer> minValuesInt;
        try
        {
            minValuesInt = minValues.stream().map(Integer::parseInt).toList();
        }
        catch (NumberFormatException exception)
        {
            throw new NumberFormatException(
                    "MinDistanceProcessingEventHandler::process: Unable to parse provided minValues list to Integer."
            );
        }

        List<Integer> maxValuesInt;
        try
        {
            maxValuesInt = maxValues.stream().map(Integer::parseInt).toList();
        }
        catch (NumberFormatException exception)
        {
            throw new NumberFormatException(
                    "MinDistanceProcessingEventHandler::process: Unable to parse provided maxValues list to Integer."
            );
        }

        return returnValues.get(getLowestDistanceIndex(minValuesInt, maxValuesInt));
    }

    /**
     * Private method to determine the index of the row that has the minimal absolute distance between the corresponding
     * {@code minValuesInt} and {@code maxValuesInt} lists.
     *
     * @param minValuesInt The events {@code minValues} list parsed to {@link List} of {@link Integer}.
     * @param maxValuesInt The events {@code maxValues} list parsed to {@link List} of {@link Integer}.
     *
     * @return The index of the row with the minimal absolute distance.
     **/
    private int getLowestDistanceIndex(List<Integer> minValuesInt, List<Integer> maxValuesInt)
    {
        int lowestDistance      = Integer.MAX_VALUE;
        int lowestDistanceIndex = Integer.MAX_VALUE;

        for (int index = 0; index < minValuesInt.size(); index++)
        {
            int currentRowMinValue = minValuesInt.get(index);
            int currentRowMaxValue = maxValuesInt.get(index);

            int distance = Math.abs(currentRowMaxValue - currentRowMinValue);

            if (distance < lowestDistance)
            {
                lowestDistance = distance;
                lowestDistanceIndex = index;
            }
        }
        return lowestDistanceIndex;
    }

}
