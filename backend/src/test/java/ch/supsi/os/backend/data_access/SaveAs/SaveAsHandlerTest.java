package ch.supsi.os.backend.data_access.SaveAs;

import ch.supsi.os.backend.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveAsHandlerTest {

    private SaveAsHandler handler1;
    private SaveAsHandler handler2;

    @Mock
    private Image mockImage;
    @Mock
    private File mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        handler1 = new SaveAsHandler() {
            @Override
            protected boolean canHandle(String format) {
                return "format1".equalsIgnoreCase(format);
            }

            @Override
            protected boolean handleSaveAs(Image image, File file, String targetFormat) throws IOException {
                return true;
            }
        };

        handler2 = new SaveAsHandler() {
            @Override
            protected boolean canHandle(String format) {
                return "format2".equalsIgnoreCase(format);
            }

            @Override
            protected boolean handleSaveAs(Image image, File file, String targetFormat) throws IOException {
                return true;
            }
        };

        handler1.setNextHandler(handler2);
    }

    @Test
    void testSaveAs_Handler1CanHandle() throws IOException {
        boolean result = handler1.saveAs(mockImage, mockFile, "format1");
        assertTrue(result);
    }

    @Test
    void testSaveAs_Handler2CanHandle() throws IOException {
        boolean result = handler1.saveAs(mockImage, mockFile, "format2");
        assertTrue(result);
    }

    @Test
    void testSaveAs_UnsupportedFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            handler1.saveAs(mockImage, mockFile, "unsupportedFormat");
        });

        assertEquals("Unsupported target format: unsupportedFormat", exception.getMessage());
    }

    @Test
    void testSetNextHandler() throws IOException {
        SaveAsHandler handler3 = mock(SaveAsHandler.class);
        when(handler3.saveAs(mockImage, mockFile, "someFormat")).thenReturn(true);

        handler2.setNextHandler(handler3);

        boolean result = handler2.saveAs(mockImage, mockFile, "someFormat");
        assertTrue(result);

        verify(handler3, times(1)).saveAs(mockImage, mockFile, "someFormat");
    }
}
