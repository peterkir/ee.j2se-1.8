# ee.j2se-1.8

## How to create a java bundle from the JDK

1. download into the folder "_download" the appropriate <linux_jdk>.tar.gz file from [Java 8 OpenJDK project](https://jdk8.java.net/) 
2. execute the launch "ExtractPublicJavaApi.launch" from project "javadoc.api.extraction"
3. run with Ant the "prepareJDK.xml" inside project "ee.j2se"
4. update the "ee.j2se/bnd.bnd" with the version
5. trigger the bnd build for "ee.j2se" (if not automatically done)

6. grep the result from the ee.j2se/generated folder