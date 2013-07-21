package ch.protolab.java.net;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;

public class HttpError extends Exception {

    private static final long serialVersionUID = -1;
    private final HttpResponse httpResponse;
    private final String body;

    HttpError(HttpResponse resp, String body) {
        super(String.format("%d %s", resp.getStatusCode(), resp.getStatusMessage()));
        httpResponse = resp;
        this.body = body;
    }

    public HttpHeaders getHeaders() {
        return httpResponse.getHeaders();
    }

    public int getStatusCode() {
        return httpResponse.getStatusCode();
    }

    public String getStatusMessage() {
        return httpResponse.getStatusMessage();
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return httpResponse.getContentType();
    }

    @Override
    public String toString() {
        return getMessage() + " " + body;
    }
}

