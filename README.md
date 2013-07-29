simple-http-client
==================

A lightweight wrapper around Google HTTP Java Client. Provides "facade" API to facilitate use of the Google HTTP Client
for common tasks.

Features
--------
- Facilitates error handling.
- Supports Java Form-based authentication (TBD).
 
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
- Add Ant build script.