package de.exxcellent.challenge.framework;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The EventDispatcher class represents a thread-safe publisher/subscriber component, which routes {@link Event} objects
 * to the {@link EventHandler} registered for the event's concrete type. <br>
 * <br>
 * Hereby, the class is implemented as a thread-safe Singleton with lazy-loading, which utilizes the double-check
 * locking pattern. Thereby, it is guaranteed that only one instance of the {@link EventDispatcher} class is created
 * and continuously reused at runtime. <br>
 * <br>
 * EventHandlers are internally maintained in a {@link ConcurrentHashMap}, so registrations and event dispatches can
 * safely occur from multiple threads without external synchronization. <br>
 *
 * @author Lukas Jeckle
 **/
public class EventDispatcher
{

    /**
     * The {@link EventDispatcher#instance} field is marked volatile to ensure proper visibility across multiple threads.
     * This ensures that the double-checked locking pattern in {@link EventDispatcher#getInstance()} works correctly.
     **/
    private static volatile EventDispatcher instance;

    private final Map<Class<? extends Event>, EventHandler<? extends Event>> handlers = new ConcurrentHashMap<>();

    /**
     * Private constructor to disable external instantiation.
     **/
    private EventDispatcher() {}

    /**
     * @return The Singleton instance of the {@link EventDispatcher} class.
     **/
    public static EventDispatcher getInstance()
    {
        /*
         Even though this local variable seems useless, it still can improve performance significantly. (Up to 40%)
           -> Performance improvement due to less volatile field accesses in cases where the Singleton is instantiated.

         Reference Link: https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
         */
        EventDispatcher result = instance;

        if (result != null)
            return result;

        synchronized (EventDispatcher.class)
        {
            if (instance == null)
                instance = new EventDispatcher();

            return instance;
        }
    }

    /**
     * Registers an {@link EventHandler} for a specific event type. <br>
     * <br>
     * <b>Important:</b>
     * <p>
     *     If a new {@link EventHandler} is registered for an event type that already had a registered handler,
     *     the old handler will simply be replaced by the new one.
     * </p>
     *
     * @param <T>       The concrete {@link Event} subclass to handle.
     * @param eventType The {@link Class} object representing the event type. (e.g.: SomeEvent.class)
     * @param handler   The {@link EventHandler} that will be used to process the events of this event type.
     **/
    public <T extends Event> void registerHandler(Class<T> eventType, EventHandler<T> handler)
    {
        handlers.put(eventType, handler);
    }

    /**
     * Dispatches an event to the handler registered for its concrete event type.
     *
     * @param <T>       The concrete {@link Event} subclass to handle.
     * @param event     The event instance to be dispatch.
     * @return          A {@link CompletableFuture} that represents the asynchronous processing of the event.
     *                  If no handler has been registered for the provided events type a completed future containing
     *                  {@code null} will be returned.
     **/
    @SuppressWarnings("unchecked")
    public <T extends Event> CompletableFuture<?> dispatchEvent(T event)
    {
        EventHandler<T> eventHandler = (EventHandler<T>) handlers.get(event.getClass());
        if (eventHandler != null)
        {
            return eventHandler.onEvent(event);
        }
        return CompletableFuture.completedFuture(null);
    }

}
