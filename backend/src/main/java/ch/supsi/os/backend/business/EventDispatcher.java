package ch.supsi.os.backend.business;

import ch.supsi.os.backend.data_access.EventListener;
import ch.supsi.os.backend.data_access.AppEventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EventDispatcher manages event subscriptions and dispatches events to registered listeners.
 * It allows components to subscribe, unsubscribe, and receive notifications for specific event types.
 */
public class EventDispatcher
{
    private final Map<AppEventType, List<EventListener<?>>> eventListeners = new HashMap<>();

    public EventDispatcher(){
    }

    public EventDispatcher(EventDispatcher other) {
        other.eventListeners.forEach((eventType, listeners) -> {
            List<EventListener<?>> newListeners = new ArrayList<>(listeners);
            eventListeners.put(eventType, newListeners);
        });
    }

    public static EventDispatcher trycopy(EventDispatcher other){
        EventDispatcher newEvent = new EventDispatcher();
        other.eventListeners.forEach(((eventType, listeners) -> listeners.forEach(l -> newEvent.subscribe(eventType,l)) ));
        return newEvent;
    }

    public void subscribe(final AppEventType eventType, final EventListener<?> listener){
        eventListeners.putIfAbsent(eventType, new ArrayList<>());
        List<EventListener<?>> listeners = eventListeners.get(eventType);
        if(listeners != null)
            listeners.add(listener);
    }

    public void unsubscribe(final AppEventType eventType, final EventListener<?> listener){
        List<EventListener<?>> listeners = eventListeners.get(eventType);
        if(listeners != null)
            listeners.remove(listener);
    }

    public <T> void dispatch(final AppEventType eventType, final T eventData) {
        final List<EventListener<?>> listeners = eventListeners.get(eventType);
        if (listeners != null) {
            for (EventListener<?> listener : listeners) {
                try {
                    ((EventListener<T>) listener).update(eventData);
                } catch (ClassCastException e) {
                    System.err.println("Error dispatching event: " + e.getMessage());
                }
            }
        }
    }

}
