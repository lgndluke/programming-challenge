package de.exxcellent.challenge.framework;

import java.time.Instant;
import java.util.UUID;

/**
 * The Event class represents an abstract event that can be used as a base class for concrete event implementations.
 * Hereby, the abstract event class directly provides unique event-ids, as well as a creation time-stamp.
 *
 * @author Lukas Jeckle
 **/
public abstract class Event
{
    private final String eventId    = UUID.randomUUID().toString();
    private final Instant eventTime = Instant.now();

    /**
     * Returns the actual runtime class of the event instance.
     *
     * @return The {@link Class} object representing the concrete subclass of the calling {@link Event} instance.
     **/
    public Class<? extends Event> getType()
    {
        return getClass();
    }

    /**
     * @return The events unique identifier ({@link UUID}) as {@link String} value.
     **/
    public String getEventId()
    {
        return eventId;
    }

    /**
     * @return The point in time ({@link Instant}) when the event was instantiated.
     **/
    public Instant getEventTime()
    {
        return eventTime;
    }

}
