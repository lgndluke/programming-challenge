package de.exxcellent.challenge.events;

import de.exxcellent.challenge.framework.Event;

import java.util.List;

/**
 * An immutable {@link Event} implementation that signals a request to start processing the minimal distance between
 * row entries of {@link List}s of {@link String} for {@code minValues} and {@code maxValues}.
 * The event returns the value of {@code returnValues} at the row-index with the processed minimal distance.
 *
 * @author Lukas Jeckle
 **/
public class MinDistanceProcessingEvent extends Event
{

    private final List<String> minValues;
    private final List<String> maxValues;
    private final List<String> returnValues;

    /**
     * @param minValues A {@link List} of {@link String} representing the minimum values for the spread calculation.
     *                  Must be parseable to {@link Integer}.
     * @param maxValues A {@link List} of {@link String} representations the maximum values for the spread calculation.
     *                  Must be parseable to {@link Integer}.
     * @param returnValues A {@link List} of {@link String} representations that serve as return keys.
     **/
    public MinDistanceProcessingEvent(List<String> minValues, List<String> maxValues, List<String> returnValues)
    {
        this.minValues    = minValues;
        this.maxValues    = maxValues;
        this.returnValues = returnValues;
    }

    /**
     * @return The {@link List} of {@link String} representations for the minimum values for the spread calculation.
     **/
    public List<String> getMinValues() {
        return minValues;
    }

    /**
     * @return The {@link List} of {@link String} representations for the maximum values of the spread calculation.
     **/
    public List<String> getMaxValues() {
        return maxValues;
    }

    /**
     * @return The {@link List} of {@link String} representations that serve as return keys.
     **/
    public List<String> getReturnValues() {
        return returnValues;
    }

}
