package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseImageSaveHandlerTest {

    private BaseImageSaveHandler handler;
    @Mock
    private ImageSaveHandler mockNextHandler;
    @Mock
    private Image mockImage;
    @Mock
    private File mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Anonymous subclass to test BaseImageSaveHandler
        handler = new BaseImageSaveHandler() {
            @Override
            public boolean save(Image image, File file) throws IOException {
                // Directly delegate to the next handler for this test
                return saveNext(image, file);
            }
        };
    }

    @Test
    void testSaveNext_WithNextHandler() throws IOException {
        // Mock behavior of the next handler
        when(mockNextHandler.save(mockImage, mockFile)).thenReturn(true);

        // Set the next handler
        handler.setNextHandler(mockNextHandler);

        // Test saveNext
        boolean result = handler.save(mockImage, mockFile);
        assertTrue(result);

        // Verify interaction with the next handler
        verify(mockNextHandler, times(1)).save(mockImage, mockFile);
    }

    @Test
    void testSaveNext_WithoutNextHandler() throws IOException {
        // Ensure there is no next handler
        handler.setNextHandler(null);

        // Test saveNext
        boolean result = handler.save(mockImage, mockFile);
        assertFalse(result); // saveNext should return false when there's no next handler
    }

    @Test
    void testSetNextHandler() {
        // Test setting the next handler
        handler.setNextHandler(mockNextHandler);
        assertNotNull(handler.nextHandler);
        assertEquals(mockNextHandler, handler.nextHandler);
    }
}
