## random prometheus java scraper endpoint
This is a port of the prometheus go scraper endpoint into java

Currently this directory does not work. I tried to use java 11 and vertex to test out small builds. However prometheus might not work with java 11 due to the deprecated `sun.misc.Unsafe`

I get this error; when running `./bin/run-module.sh`
```
Caused by: java.lang.ClassNotFoundException: sun.misc.Unsafe
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:583)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)
        ... 38 more
```