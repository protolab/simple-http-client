simple-http-client
==================

Provides the "façade" API for the core parts of the Google HTTP Java Client library.

Samples
-------
* Minimal example.
```java
HttpClient c = HttpClient.newInstance();
GenericUrl url = new GenericUrl("http://en.wikipedia.org/wiki/Special:Random");
HttpResponse resp = c.get(url);
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
- Google HTTP Java Client: https://github.com/google/google-http-java-client
