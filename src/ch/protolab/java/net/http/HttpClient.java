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
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * A lightweight wrapper around <a href="https://code.google.com/p/google-http-java-client/">Google HTTP Java Client</a>.</br>
 * java.net.HttpURLConnection is used as low-level HTTP transport.<br/><br/>
 * <p/>
 * Supports GET, HEAD, POST, PUT, DELETE methods. Accepts ALL cookies.<br/>
 * Sample usage:<br/>
 * <code>
 * Map<String, Object> params = new HashMap<>();<br/>
 * params.put("term", "http");<br/>
 * params.put("short", true);<br/>
 * GenericUrl url = new GenericUrl(UriTemplate.expand("http://example.com/", "tags/{term}", params, true));<br/>
 * <br/>
 * HttpResponse resp = HttpClient.newInstance().get(url);<br/>
 * if (resp.isSuccessStatusCode())<br/>
 * // process the response<br/>
 * else<br/>
 * // handle an HTTP error response<br/>
 * </code>
 */
public final class HttpClient {

    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    static {
        // Set cookie policy
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
    }

    private final HttpRequestFactory factory;
    private final boolean followRedirects;

    HttpClient(final Builder b) {
        this.followRedirects = b.followRedirects;
        this.factory = TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest req) throws IOException {
                req.setThrowExceptionOnExecuteError(false);
                req.setCurlLoggingEnabled(false);

                req.setConnectTimeout(b.timeout);
                req.setFollowRedirects(b.followRedirects);
                req.setHeaders(b.headers);
                if (b.headers.getUserAgent() != null)
                    req.setSuppressUserAgentSuffix(true);
                req.setReadTimeout(b.readTimeout);
            }
        });
    }

    public static HttpClient newInstance() {
        return new Builder().build();
    }

    /**
     * Sends GET request.
     *
     * @param url request URL
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse get(GenericUrl url) throws IOException {
        return get(url, null, followRedirects);
    }

    /**
     * Sends GET request.
     *
     * @param url     request URL
     * @param headers additional headers to be sent along with the common headers sent by the client.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse get(GenericUrl url, HttpHeaders headers) throws IOException {
        return get(url, headers, followRedirects);
    }

    /**
     * Sends GET request.
     *
     * @param url             request URL
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse get(GenericUrl url, boolean followRedirects) throws IOException {
        return get(url, null, followRedirects);
    }

    /**
     * Sends GET request.
     *
     * @param url             request URL
     * @param headers         additional headers to be sent along with the common headers sent by the client.
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse get(GenericUrl url, HttpHeaders headers, boolean followRedirects) throws IOException {
        return send(url, "GET", headers, followRedirects, null);
    }

    /**
     * Sends HEAD request.
     *
     * @param url request URL
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse head(GenericUrl url) throws IOException {
        return head(url, null, followRedirects);
    }

    /**
     * Sends HEAD request.
     *
     * @param url     request URL
     * @param headers additional headers to be sent along with the common headers sent by the client.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse head(GenericUrl url, HttpHeaders headers) throws IOException {
        return head(url, headers, followRedirects);
    }

    /**
     * Sends HEAD request.
     *
     * @param url             request URL
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse head(GenericUrl url, boolean followRedirects) throws IOException {
        return head(url, null, followRedirects);
    }

    /**
     * Sends HEAD request.
     *
     * @param url             request URL
     * @param headers         additional headers to be sent along with the common headers sent by the client.
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse head(GenericUrl url, HttpHeaders headers, boolean followRedirects) throws IOException {
        return send(url, "HEAD", headers, followRedirects, null);
    }

    /**
     * Sends DELETE request.
     *
     * @param url request URL
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse delete(GenericUrl url) throws IOException {
        return delete(url, null, followRedirects);
    }

    /**
     * Sends DELETE request.
     *
     * @param url             request URL
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse delete(GenericUrl url, boolean followRedirects) throws IOException {
        return delete(url, null, followRedirects);
    }

    /**
     * Sends DELETE request.
     *
     * @param url     request URL
     * @param headers additional headers to be sent along with the common headers sent by the client.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse delete(GenericUrl url, HttpHeaders headers) throws IOException {
        return delete(url, headers, followRedirects);
    }

    /**
     * Sends DELETE request.
     *
     * @param url             request URL
     * @param headers         additional headers to be sent along with the common headers sent by the client.
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse delete(GenericUrl url, HttpHeaders headers, boolean followRedirects) throws IOException {
        return send(url, "DELETE", headers, followRedirects, null);
    }

    /**
     * Sends POST  request.
     *
     * @param url  request URL
     * @param body Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse post(GenericUrl url, HttpContent body) throws IOException {
        return post(url, null, followRedirects, body);
    }

    /**
     * Sends POST  request.
     *
     * @param url             request URL
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @param body            Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse post(GenericUrl url, HttpContent body, boolean followRedirects) throws IOException {
        return post(url, null, followRedirects, body);
    }

    /**
     * Sends POST  request.
     *
     * @param url     request URL
     * @param headers additional headers to be sent along with the common headers sent by the client.
     * @param body    Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse post(GenericUrl url, HttpContent body, HttpHeaders headers) throws IOException {
        return post(url, headers, followRedirects, body);
    }

    /**
     * Sends POST  request.
     *
     * @param url             request URL
     * @param headers         additional headers to be sent along with the common headers sent by the client.
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @param body            Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse post(GenericUrl url, HttpHeaders headers, boolean followRedirects, HttpContent body) throws
                                                                                                             IOException {
        requireNonNull(body, "Request body must not be null!");
        return send(url, "POST", headers, followRedirects, body);
    }

    /**
     * Sends PUT  request.
     *
     * @param url  request URL
     * @param body Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse put(GenericUrl url, HttpContent body) throws IOException {
        return put(url, null, followRedirects, body);
    }

    /**
     * Sends PUT  request.
     *
     * @param url             request URL
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @param body            Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse put(GenericUrl url, HttpContent body, boolean followRedirects) throws IOException {
        return put(url, null, followRedirects, body);
    }

    /**
     * Sends PUT  request.
     *
     * @param url     request URL
     * @param headers additional headers to be sent along with the common headers sent by the client.
     * @param body    Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse put(GenericUrl url, HttpContent body, HttpHeaders headers) throws IOException {
        return put(url, headers, followRedirects, body);
    }

    /**
     * Sends PUT  request.
     *
     * @param url             request URL
     * @param headers         additional headers to be sent along with the common headers sent by the client.
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @param body            Request payload to be sent to the server.
     * @return an HttpResponse instance.
     * @throws IOException thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     */
    public HttpResponse put(GenericUrl url, HttpHeaders headers, boolean followRedirects, HttpContent body) throws
                                                                                                            IOException {
        requireNonNull(body, "Request body must not be null!");
        return send(url, "PUT", headers, followRedirects, body);
    }

    /**
     * Sends an HTTP request.
     *
     * @param url             request URL
     * @param method          HTTP method.
     * @param headers         additional headers to be sent along with the common headers sent by the client. Can be {@code null}.
     * @param followRedirects indicates whether the redirect responses should be handled automatically.
     * @param body            Request payload to be sent to the server. Can be {@code null}.
     * @return an HttpResponse instance.
     * @throws IOException              thrown if an I/O exception is thrown by the low-level java.net.URLConnection or java.net.URL.
     * @throws IllegalArgumentException if {@code body} supplied with either GET, HEAD, or DELETE request.
     */
    HttpResponse send(
            GenericUrl url,
            String method,
            HttpHeaders headers,
            boolean followRedirects,
            HttpContent body) throws IOException {

        requireNonNull(url, "Request URL instance must not be null!");

        if (("GET".equals(method) || "HEAD".equals(method) || "DELETE".equals(method)) && body != null)
            throw new IllegalArgumentException("Body must not be supplied for GET/HEAD/DELETE request!");

        HttpRequest req = factory.buildRequest(method, url, body);
        if (headers != null)
            req.getHeaders().putAll(headers);

        req.setFollowRedirects(followRedirects);

        return req.execute();
    }

    /**
     * Constructs an HttpClient instance.
     */
    public static final class Builder {

        boolean followRedirects = true;
        int timeout = 0;
        HttpHeaders headers = new HttpHeaders();
        int readTimeout = 0;

        /**
         * Sets User-Agent header.
         *
         * @param ua User Agent string.
         * @return a {@code Builder} instance.
         */
        public Builder userAgent(String ua) {
            if (ua != null && !ua.isEmpty())
                this.headers.setUserAgent(ua);
            return this;
        }

        /**
         * Sets connection timeout.
         *
         * @param value connection timeout.
         * @param unit  time interval unit.
         * @return a {@code Builder} instance.
         */
        public Builder timeout(int value, TimeUnit unit) {
            this.timeout = (int) unit.toMillis(value);
            return this;
        }

        /**
         * Sets read timeout.
         *
         * @param value read timeout.
         * @param unit  time interval unit.
         * @return a {@code Builder} instance.
         */
        public Builder readTimeout(int value, TimeUnit unit) {
            this.readTimeout = (int) unit.toMillis(value);
            return this;
        }

        /**
         * Sets {@code Accept} header which is common for all the requests sent by this instance of HttpClient.
         *
         * @param mimeType MIME type as described in https://tools.ietf.org/html/rfc2046
         * @return a {@code Builder} instance.
         */
        public Builder accept(String mimeType) {
            if (mimeType != null && !mimeType.isEmpty())
                this.headers.setAccept(mimeType);
            return this;
        }

        /**
         * Indicates whether the HttpClient follows redirect HTTP 301, 302, 307 responses. Applied for all requests.
         *
         * @param val {@code true} or {@code false}
         * @return a {@code Builder} instance.
         */
        public Builder followRedirects(boolean val) {
            this.followRedirects = val;
            return this;
        }

        /**
         * Adds http request headers which are common for all the requests sent by this instance of HttpClient.
         *
         * @param name  header name
         * @param value header value
         * @return a {@code Builder} instance.
         */
        public Builder header(String name, Object value) {
            this.headers.set(name, value);
            return this;
        }

        /**
         * Constructs {@code HttpClient} object.
         *
         * @return a {@code HttpClient} instance.
         */
        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}