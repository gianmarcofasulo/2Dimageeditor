package ch.supsi.os.backend.business;

import ch.supsi.os.backend.data_access.AppEventType;
import ch.supsi.os.backend.data_access.EventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventDispatcherTest {

    private EventDispatcher eventDispatcher;

    @BeforeEach
    void setUp() {
        eventDispatcher = new EventDispatcher();
    }

    @Test
    void testSubscribeAndDispatch() {
        // Mock listener
        EventListener<String> mockListener = mock(EventListener.class);

        // Subscribe to an event
        eventDispatcher.subscribe(AppEventType.FILE_OPENED, mockListener);

        // Dispatch the event
        eventDispatcher.dispatch(AppEventType.FILE_OPENED, "File1.txt");

        // Verify the listener received the event
        verify(mockListener, times(1)).update("File1.txt");
    }

    @Test
    void testUnsubscribe() {
        // Mock listener
        EventListener<String> mockListener = mock(EventListener.class);

        // Subscribe to an event
        eventDispatcher.subscribe(AppEventType.FILE_OPENED, mockListener);

        // Unsubscribe from the event
        eventDispatcher.unsubscribe(AppEventType.FILE_OPENED, mockListener);

        // Dispatch the event
        eventDispatcher.dispatch(AppEventType.FILE_OPENED, "File1.txt");

        // Verify the listener did not receive the event
        verify(mockListener, never()).update(anyString());
    }

    @Test
    void testDispatchWithNoListeners() {
        // Dispatch an event with no listeners
        assertDoesNotThrow(() -> eventDispatcher.dispatch(AppEventType.FILE_OPENED, "File1.txt"));
    }

    @Test
    void testTryCopy() {
        // Mock listener
        EventListener<String> mockListener = mock(EventListener.class);

        // Subscribe to an event in the original dispatcher
        eventDispatcher.subscribe(AppEventType.FILE_SAVED, mockListener);

        // Create a copy of the dispatcher
        EventDispatcher copiedDispatcher = EventDispatcher.trycopy(eventDispatcher);

        // Dispatch the event in the copied dispatcher
        copiedDispatcher.dispatch(AppEventType.FILE_SAVED, "File2.txt");

        // Verify the listener in the copied dispatcher received the event
        verify(mockListener, times(1)).update("File2.txt");
    }

    @Test
    void testEventDispatcherCopyConstructor() {
        // Mock listener
        EventListener<String> mockListener = mock(EventListener.class);

        // Subscribe to an event in the original dispatcher
        eventDispatcher.subscribe(AppEventType.FILE_SAVED_AS, mockListener);

        // Create a copy using the copy constructor
        EventDispatcher copiedDispatcher = new EventDispatcher(eventDispatcher);

        // Dispatch the event in the copied dispatcher
        copiedDispatcher.dispatch(AppEventType.FILE_SAVED_AS, "File3.txt");

        // Verify the listener in the copied dispatcher received the event
        verify(mockListener, times(1)).update("File3.txt");
    }

    @Test
    void testDispatchWithClassCastException() {
        // Mock listener
        @SuppressWarnings("unchecked")
        EventListener<Integer> mockListener = mock(EventListener.class);

        // Subscribe listener
        eventDispatcher.subscribe(AppEventType.LANGUAGE_CHANGED, mockListener);

        // Dispatch incompatible event
        eventDispatcher.dispatch(AppEventType.LANGUAGE_CHANGED, "English");

        // Capture arguments passed to the update method
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);

        // Verify that update was never called
        verify(mockListener, never()).update(captor.capture());
    }
}
