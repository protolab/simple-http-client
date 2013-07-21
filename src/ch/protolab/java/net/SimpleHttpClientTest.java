package ch.protolab.java.net;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SimpleHttpClientTest {

    @Test(expected = HttpError.class)
    public void testGetText_Error() throws Exception {
        SimpleHttpClient client = new SimpleHttpClient();
        client.getText(URI.create("http://www.inprodicon.com/abc"));
    }

    @Test
    public void testGetText_404_Error() throws IOException {
        SimpleHttpClient client = new SimpleHttpClient();
        try {
            client.getText(URI.create("http://www.inprodicon.com/abc"));
        }
        catch (HttpError error) {
            assertEquals(404, error.getStatusCode());
            assertEquals("Not Found", error.getStatusMessage());
            assertEquals("text/html; charset=UTF-8", error.getContentType());
        }
    }

    @Test
    public void testGetText_Custom_Error_Handler() throws Exception {
        fail();
    }
}
