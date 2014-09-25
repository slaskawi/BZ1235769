Customized CacheStore for client/server mode
============================================
Author: Wolf-Dieter Fink
Level: Adcanced
Technologies: Infinispan


What is it?
-----------
The project show how to implement a CustomCache store as a module and use it as a module in JDG.
If this is used in JDG it is current not supported and a SupportException is necessary!

Build the project
-----------------
1. Type this command to build and deploy the archive:

        mvn clean package


Configure JDG
-------------

1. add the SimpleCustomCacheStore as module via CLI

    module add --name=org.infinispan.wfink.persistence --resources=CustomCacheStore.jar --dependencies=org.infinispan

2. In client server mode this module need to be in the classpath for the infinispan.core module.
   change the modules/system/layers/base/org/infinispan/main/module.xml

       <module xmlns="urn:jboss:module:1.1" name="org.infinispan">
           <resources ... />
           <dependencies>
               <module name="org.infinispan.wfink.persistence"/>

3. Add a cache with the store

* Infinispan subsystem definition:

        <subsystem xmlns="urn:infinispan:server:core:6.1" default-cache-container="local">
            <cache-container name="local" default-cache="default" statistics="true">
                <local-cache name="SimpleCacheStore" start="EAGER">
                    <store class="org.infinispan.wfink.persistence.SimpleCustomCacheStore" shared="false" preload="true" passivation="true">
                        <property name="path">wfFileStore</property>
                    </store>
                </local-cache>


Run the client application
-------------
1. Change to the client directory and type this command to start the client application

        mvn exec:java

