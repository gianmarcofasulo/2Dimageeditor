package ch.supsi.os.backend.data_access.Save;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSaveHandlerTest {

    private DefaultSaveHandler defaultSaveHandler;

    @Mock
    private Image mockImage;
    @Mock
    private File mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        defaultSaveHandler = new DefaultSaveHandler();
    }

    @Test
    void testSave_ReturnsFalse() {
        boolean result = defaultSaveHandler.save(mockImage, mockFile);
        assertFalse(result, "DefaultSaveHandler should always return false.");
    }

    @Test
    void testSetNextHandler() {
        ImageSaveHandler mockNextHandler = new DefaultSaveHandler();
        defaultSaveHandler.setNextHandler(mockNextHandler);

        assertNotNull(defaultSaveHandler.nextHandler, "Next handler should be set.");
        assertEquals(mockNextHandler, defaultSaveHandler.nextHandler, "Next handler should match the set handler.");
    }
}
