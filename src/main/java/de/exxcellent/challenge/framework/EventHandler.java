package de.exxcellent.challenge.framework;

import java.util.concurrent.CompletableFuture;

/**
 * The EventHandler interface represents a consumer of {@link Event} objects.
 * Implementations are responsible for processing an event of a specific concrete type {@code T} and returning a {@link
 * CompletableFuture} that supplies the asynchronous processing result.
 *
 * @param <T> The concrete {@link Event} subclass which the EventHandler implementation is meant to process.
 *
 * @author Lukas Jeckle
 **/
public interface EventHandler<T extends Event>
{

    /**
     * Processes the supplied {@link Event}.
     *
     * @param event The event instance to be processed.
     * @return      A {@link CompletableFuture} that represents the asynchronous result from processing the event.
     **/
    CompletableFuture<?> onEvent(T event);

}
