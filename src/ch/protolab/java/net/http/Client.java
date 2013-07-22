/*
 *  Copyright 2012 Michael Krasnovsky
 *
 *  Licensed under the The MIT License (MIT) (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ch.protolab.java.net.http;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static java.util.Objects.requireNonNull;

/**
 * A lightweight wrapper around <a href="https://code.google.com/p/google-http-java-client/">Google HTTP Java Client</a>.</br>
 * java.net.HttpURLConnection is used as low-level HTTP transport.<br/><br/>
 *
 * Supports GET, HEAD, POST, PUT, DELETE methods. Accepts ALL cookies.<br/>
 * Sample usage:<br/>
 * <code>
 *     Map<String, Object> params = new HashMap<>();<br/>
 *     params.put("term", "http");<br/>
 *     params.put("short", true);<br/>
 *     GenericUrl url = new GenericUrl(UriTemplate.expand("http://example.com/", "tags/{term}", params, true));<br/>
 *     <br/>
 *     HttpResponse resp = Client.get(url);<br/>
 *     if (resp.isSuccessStatusCode())<br/>
 *         // process the response<br/>
 *     else<br/>
 *         // handle an HTTP error response<br/>
 * </code>
 */
public final class Client {

    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private static final HttpRequestFactory FACTORY;
    static {
        FACTORY = TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest req) throws IOException {
                req.setThrowExceptionOnExecuteError(false);
//                req.setLoggingEnabled(false);
                req.setCurlLoggingEnabled(false);
            }
        });

        // Set cookie policy
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
    }

    public static HttpResponse get(GenericUrl url) throws IOException {
        return send(url, "GET", null);
    }

    public static HttpResponse head(GenericUrl url) throws IOException {
        return send(url, "HEAD", null);
    }

    public static HttpResponse delete(GenericUrl url) throws IOException {
        return send(url, "DELETE", null);
    }

    public static HttpResponse post(GenericUrl url, HttpContent body) throws IOException {
        requireNonNull(body, "Request body must not be null!");
        return send(url, "POST", body);
    }

    public static HttpResponse put(GenericUrl url, HttpContent body) throws IOException {
        requireNonNull(body, "Request body must not be null!");
        return send(url, "PUT", body);
    }

    static HttpResponse send(GenericUrl url, String method, HttpContent body) throws IOException {
        requireNonNull(url, "Request URL instance must not be null!");

        if (("GET".equals(method) || "HEAD".equals(method) || "DELETE".equals(method)) && body != null)
            throw new IllegalArgumentException("Body must not be supplied for GET/HEAD/DELETE request!");

        HttpRequest req = FACTORY.buildRequest(method, url, body);
        return req.execute();
    }

    /**
     * Disable logging in form of cURL commands
     * Do not throw HttpResponse exception on HTTP errors >= 400
     * Invokes user's error handler, if provided.
     */
    private static final class InitializerImpl implements ClientInitializer {

        private ClientInitializer initializer;

        InitializerImpl() {
        }

        InitializerImpl(ClientInitializer initializer) {
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