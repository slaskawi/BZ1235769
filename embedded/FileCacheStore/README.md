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

2. Install the Infinispan/JDG modules to WildFly or EAP server and deploy the application

       unzip jboss-datagrid-6.3.0-eap-modules-library.zip
       mv jboss-datagrid-6.3.0-eap-modules-library/modules/org SERVER/modules
       cp web/target/embeddedFileStoreApp-web.war SERVER/standalone/deployments

3. Start a WildFly or EAP server and deploy the application

       cp web/target/embeddedFileStoreApp-web.war SERVER/standalone/deployments

4. Open a Browser and access the application

      http://localhost:8080/embeddedFileStoreApp-web

