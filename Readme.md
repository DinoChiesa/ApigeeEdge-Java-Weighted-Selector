# Java Weighted Selector callout

This directory contains the Java source code and pom.xml file required to
compile a simple Java callout for Apigee Edge. The callout is performs a weighted random selection,
based on inputs.

## Building:

1. unpack (if you can read this, you've already done that).

2. configure the build on your machine by loading the Apigee jars into your local cache
  ```./buildsetup.sh```

2. Build with maven.
  ```mvn clean package```

3. if you edit proxy bundles offline, copy the resulting jar file, available in  target/httpsig-edge-callout.jar to your apiproxy/resources/java directory.  If you don't edit proxy bundles offline, upload the jar file into the API Proxy via the Edge API Proxy Editor .

4. include an XML file for the Java callout policy in your
   apiproxy/resources/policies directory. It should look
   like this:
   ```xml
   <JavaCallout name='Java-WeightedSelector'>
     <Properties>
       <Property name="weights">0, 1, 1, 2, 3, 5, 8, 13, 21, 34</Property>
     </Properties>
     <ClassName>com.google.apigee.callout.WeightedSelectorCallout</ClassName>
     <ResourceURL>java://edge-custom-weighted-selector-1.0.1.jar</ResourceURL>
   </JavaCallout>
   ```

5. use the Edge UI, or a command-line tool like pushapi (See
   https://github.com/carloseberhardt/apiploy) or similar to
   import the proxy into an Edge organization, and then deploy the proxy .
   Eg,
   ```./pushapi -v -d -o ORGNAME -e test -n weighted-selector bundle```

6. Use a client to generate and send http requests to the proxy. Eg,
   ```curl -i http://ORGNAME-test.apigee.net/weighted-selector/t1```




## Dependencies

- Apigee Edge expressions v1.0
- Apigee Edge message-flow v1.0
- Google Guava v20.0

These jars must be available on the classpath for the compile to
succeed. The buildsetup.sh script will download these files for
you automatically, and will insert them into your maven cache.

## Notes

There is one callout class, com.google.apigee.callout.WeightedSelectorCallout,
which does the weighted selection and then inserts the computed bucket value into a context variable.


## Example Usage

```
$ curl -i https://ORGNAME-test.apigee.net/nanotime/t1
HTTP/1.1 200 OK
Date: Tue, 10 Nov 2015 19:41:39 GMT
Content-Type: application/json
Content-Length: 161
Connection: keep-alive
Server: Apigee Router

{
  "message" : "ok",
  "system.uuid" : "ac4185b1-f5ee-4997-9448-00399715f2f4",
  "bucket" : "8",
  "now" : "Thu, 3 Aug 2017 18:52:49 UTC"
}

```

## LICENSE

This material is [Copyright (c) 2017 Google LLC](NOTICE).
and is licensed under the Apache 2.0 license. See the [LICENSE](LICENSE) file.


## Bugs

There are few unit tests for this project.
