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

class SaveAsChainTest {

    @Mock
    private SaveAsHandler mockHandler;
    @Mock
    private Image mockImage;
    @Mock
    private File mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAs_ValidFormatHandledByChain() throws IOException {
        // Mock behavior
        when(mockHandler.saveAs(mockImage, mockFile, "FORMAT1")).thenReturn(true);

        // Create a chain with the mock handler
        SaveAsChain saveAsChain = new SaveAsChain(mockHandler);

        // Test the saveAs method
        boolean result = saveAsChain.saveAs(mockImage, mockFile, "format1");
        assertTrue(result);

        // Verify the handler was called
        verify(mockHandler, times(1)).saveAs(mockImage, mockFile, "FORMAT1");
    }

    @Test
    void testSaveAs_InvalidFormatThrowsException() throws IOException {
        // Mock behavior to throw an exception
        when(mockHandler.saveAs(mockImage, mockFile, "UNSUPPORTED")).thenThrow(new IllegalArgumentException("Unsupported target format: UNSUPPORTED"));

        // Create a chain with the mock handler
        SaveAsChain saveAsChain = new SaveAsChain(mockHandler);

        // Test the saveAs method for an unsupported format
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            saveAsChain.saveAs(mockImage, mockFile, "unsupported");
        });

        assertEquals("Unsupported target format: UNSUPPORTED", exception.getMessage());

        // Verify the handler was called
        verify(mockHandler, times(1)).saveAs(mockImage, mockFile, "UNSUPPORTED");
    }

    @Test
    void testSaveAs_TrimAndNormalizeTargetFormat() throws IOException {
        // Mock behavior
        when(mockHandler.saveAs(mockImage, mockFile, "NORMALIZED")).thenReturn(true);

        // Create a chain with the mock handler
        SaveAsChain saveAsChain = new SaveAsChain(mockHandler);

        // Test with untrimmed and lowercase format
        boolean result = saveAsChain.saveAs(mockImage, mockFile, "  normalized  ");
        assertTrue(result);

        // Verify the handler was called with the normalized format
        verify(mockHandler, times(1)).saveAs(mockImage, mockFile, "NORMALIZED");
    }
}
