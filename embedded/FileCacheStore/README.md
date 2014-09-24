Infinispan application with embedded cache: Use of Library mode and EAP modules
===============================================================================
Author: Wolf-Dieter Fink
Level: Adcanced
Technologies: Infinispan, Web application


What is it?
-----------

Show how a web application can create a Infinispan cache in library mode configured with XML files.

Build and Run
-------------
1. Type this command to build and deploy the archive:

        mvn clean package

2. Start a WildFly or EAP server and deploy the application

       cp web/target/embeddedFileStoreApp-web.war SErVER/standalone/deployments

3. Open a Browser and access the application

      http://localhost:8080/embeddedFileStoreApp-web

