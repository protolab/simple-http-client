package ch.protolab.java.net;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;

public interface HttpClientInitializer extends HttpRequestInitializer, HttpUnsuccessfulResponseHandler {

}
