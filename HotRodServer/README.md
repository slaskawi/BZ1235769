HotRod embedded server: Use caches with an embedded HotRod server
=================================================================
Author: Wolf-Dieter Fink, Tristan Tarrant
Level: Adcanced
Technologies: Infinispan, Hot Rod


What is it?
-----------

Hot Rod is a binary TCP client-server protocol. The Hot Rod protocol facilitates faster client and server interactions in comparison to other text based protocols and allows clients to make decisions about load balancing, failover and data location operations.

This example demonstrates how to start a HotRod server as a Java standalone programm to store, retrieve, and remove data from cache using a Hot Rod client.
Also the necessity to implement a speicial Equivalence class to compare the objects stored at server side.
Otherwise it is not possible to retrive the values by the apropriate key.
This is done by default if an Infinispan/JDG server is used, see https://github.com/infinispan/infinispan/blob/master/server/integration/infinispan/src/main/resources/infinispan-defaults.xml


Build and Run as Standalone server
-------------
1. Type this command to build and deploy the archive:

        mvn clean package

2. Change to the server directory and start the HotRod server in standalone mode

       mvn exec:java

3. Open a new shell and change to the client directory and run the HotRod client

      mvn exec:java

4. Compare the client output

      Inserting data into cache...
      two -> Second Value
      seven -> Seventh Value
      five -> Fifth Value
      one -> First Value
      three -> Third Value
      four -> Fourth Value
      six -> Sixth Value

      Verifying data...
      verify key two ok
      verify key seven ok
      verify key five ok
      verify key one ok
      verify key three ok
      verify key four ok
      verify key six ok

5. Switch to the first shell and type CTRL-C to stop the standalone server

