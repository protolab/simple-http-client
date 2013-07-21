package ch.protolab.java.net;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;

/**
 * A lightweight wrapper around <a href="https://code.google.com/p/google-http-java-client/">Google HTTP Java Client</a>.</br>
 * Supports GET, HEAD, POST, PUT, DELETE methods.
 */
public class SimpleHttpClient {

    static {
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
    }

    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private final HttpRequestFactory factory;

    public SimpleHttpClient() {
        factory = TRANSPORT.createRequestFactory(new InitializerImpl());
    }

    public SimpleHttpClient(HttpClientInitializer initializer) {
        factory = TRANSPORT.createRequestFactory(new InitializerImpl(initializer));
    }

    public byte[] getBinary(URI location) {
        return null;
    }

    public String getText(URI location) throws IOException, HttpError {
        HttpRequest req = factory.buildRequest("GET", new GenericUrl(location), null);
        HttpResponse resp = req.execute();
        String content = resp.parseAsString();
        if (!resp.isSuccessStatusCode())
            throw new HttpError(resp, content);
        return content;
    }

    public byte[] getBinary(URI location, HttpUnsuccessfulResponseHandler handler) {
        return null;
    }

    public String getText(URI location, HttpUnsuccessfulResponseHandler handler) {
        return null;
    }

    /**
     * Disable logging in form of cURL commands
     * Do not throw HttpResponse exception on HTTP errors >= 400
     */
    private static final class InitializerImpl implements HttpClientInitializer {

        private HttpClientInitializer initializer;

        InitializerImpl() {
        }

        InitializerImpl(HttpClientInitializer initializer) {
            this.initializer = initializer;
        }

        @Override
        public void initialize(HttpRequest req) throws IOException {
            req.setThrowExceptionOnExecuteError(false);
            req.setUnsuccessfulResponseHandler(this);
            req.setCurlLoggingEnabled(false);

            if (initializer != null)
                initializer.initialize(req);
        }

        @Override
        public boolean handleResponse(HttpRequest req, HttpResponse resp, boolean retrySupported) throws IOException {
            return initializer != null && initializer.handleResponse(req, resp, retrySupported);
        }
    }
}
