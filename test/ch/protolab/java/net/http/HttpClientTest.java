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

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HttpClientTest {

    private static final String CHROME_UA = "Mozilla/5.0 (X11; Linux x86_64) " +
                                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                            "Chrome/28.0.1500.71 Safari/537.36";

    private GenericUrl testUrl = new GenericUrl("http://www.wikipedia.org");

    @Test
    public void test_Custom_UserAgent() throws IOException {
        String ua = "TestClient/1.0";
        HttpClient c = new HttpClient.Builder().userAgent(ua).build();
        HttpResponse resp = c.head(testUrl);
        assertEquals(ua, resp.getRequest().getHeaders().getUserAgent());
    }

    @Test
    public void test_Override_Common_Headers() throws IOException {
        HttpClient c = new HttpClient.Builder().accept("*/*").build();
        String expected = "text/html";
        HttpHeaders hh = new HttpHeaders();
        hh.setAccept(expected);
        HttpResponse resp = c.head(testUrl, hh);
        assertEquals(expected, resp.getRequest().getHeaders().getAccept());
    }


    @Test
    public void test_Add_To_Common_Headers() throws IOException {
        String expectedAccept = "text/html";
        HttpClient c = new HttpClient.Builder().userAgent(CHROME_UA).accept(expectedAccept).build();
        String expectedKey = "1234x";
        HttpHeaders hh = new HttpHeaders();
        String apiKeyHeader = "X-API-Key";
        hh.put(apiKeyHeader, expectedKey);
        HttpResponse resp = c.head(testUrl, hh);

        assertEquals(expectedKey, resp.getRequest().getHeaders().get(apiKeyHeader));
        assertEquals(expectedAccept, resp.getRequest().getHeaders().getAccept());
    }
}
