package ch.supsi.os.backend.data_access.Loader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class ImageLoaderChainTest {

    private ImageLoaderChain imageLoaderChain;

    @BeforeEach
    void setUp() {
        imageLoaderChain = new ImageLoaderChain();
    }

    @Test
    void testHandlerChainInitialization() throws NoSuchFieldException, IllegalAccessException {
        // Get the first handler
        BaseImageLoadHandler handlerChain = imageLoaderChain.getHandlerChain();

        // Verify the first handler is PbmLoadHandler
        assertTrue(handlerChain instanceof PbmLoadHandler, "First handler should be PbmLoadHandler");

        // Use reflection to access the private 'nextHandler' field
        Field nextHandlerField = BaseImageLoadHandler.class.getDeclaredField("nextHandler");
        nextHandlerField.setAccessible(true);

        // Verify the second handler is PgmLoadHandler
        BaseImageLoadHandler secondHandler = (BaseImageLoadHandler) nextHandlerField.get(handlerChain);
        assertNotNull(secondHandler, "Second handler should not be null");
        assertTrue(secondHandler instanceof PgmLoadHandler, "Second handler should be PgmLoadHandler");

        // Verify the third handler is PpmLoadHandler
        BaseImageLoadHandler thirdHandler = (BaseImageLoadHandler) nextHandlerField.get(secondHandler);
        assertNotNull(thirdHandler, "Third handler should not be null");
        assertTrue(thirdHandler instanceof PpmLoadHandler, "Third handler should be PpmLoadHandler");

        // Verify the fourth handler is DefaultLoadHandler
        BaseImageLoadHandler fourthHandler = (BaseImageLoadHandler) nextHandlerField.get(thirdHandler);
        assertNotNull(fourthHandler, "Fourth handler should not be null");
        assertTrue(fourthHandler instanceof DefaultLoadHandler, "Fourth handler should be DefaultLoadHandler");

        // Verify there is no handler after the default handler
        BaseImageLoadHandler fifthHandler = (BaseImageLoadHandler) nextHandlerField.get(fourthHandler);
        assertNull(fifthHandler, "DefaultLoadHandler should not have a next handler");
    }
}
