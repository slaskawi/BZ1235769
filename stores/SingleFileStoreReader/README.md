Standalone program to read a standard SingleFileStore
=====================================================
Author: Wolf-Dieter Fink
Level: Simple
Technologies: Infinispan


What is it?
-----------

A simple implementation to read a stored Infinispan cache from a file.
The logic to read the file is copied from the original implementation which work since JDG6.2, but might change in the future.

Build and Run
-------------
1. Type this command to build and deploy the archive:

        mvn clean package

2. Read a SingleFileStore

      Start the following command :

          JDG_LIB=/path/to/JDG/libraries startCache.sh [-show-key] [-show-data] <singleFileStore.dat>

      If no options are given the file is scanned, meta data of each entry is listed and a summary is shown how many entries are in the file.
         -show-key  unmarshall the keys with jboss-marshalling and print it
         -show-data unmarshall the data using jboss-marshalling and print it
         -no-meta   supress list of meta data for each entry
