package ch.supsi.os.backend.data_access;

/**
 *
 * This interface allows you to create event listeners that can handle events of different types.
 * For example, you could have event listeners that handle events related to game states, player actions, file operations, etc.
 * Each listener would specify the type of data it expects to receive in the update method.
 */
public interface EventListener<T>
{
    void update(final T data);
}
