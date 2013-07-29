simple-http-client
==================

Provides the "façade" API for the core parts of the Google HTTP Java Client library.

Samples
-------
* Minimal example.
```java
HttpClient c = HttpClient.newInstance();
HttpResponse resp = c.get(new GenericUrl("http://en.wikipedia.org/wiki/Special:Random"));
if (resp.isSuccessStatusCode())
    // process the response
else
    // handle an HTTP error response
```

* More advanced
```java
HttpClient c = new HttpClient.Builder()
                 .userAgent("HttpClient/1.0.0 (Google Java HTTP Client)")
                 .accept("application/json")
                 .build();
GenericUrl url = new GenericUrl("https://api.github.com/users/protolab");
HttpResponse resp = c.get(url);
if (resp.isSuccessStatusCode())
    // process JSON response
else
    // handle an HTTP error response
```

Dependencies
------------
- Google HTTP Java Client: https://code.google.com/p/google-http-java-client

TODO
----
- Add HttpClient.Builder.beforeRequest(HttpRequestInterceptor) and HttpClient.Builder.afterRequest(HttpResponseInterceptor)
- Modify Ant build script (should be run outside IDEA IDE, add Javadoc generation).