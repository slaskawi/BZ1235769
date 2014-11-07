Standalone application with Infinispan cache
============================================
Author: Wolf-Dieter Fink
Level: Simple
Technologies: Infinispan


What is it?
-----------

Show how a standalone application can create a Infinispan cache in library mode configured it.

Build and Run
-------------
1. Type this command to build and deploy the archive:

        mvn clean package

2. Start an instance

      create a folder to store the file of the SingleFileStore
      WORKAROUND  change the classpath of run.sh appropriate to your system

          start run.sh -repl -async -preload -passivation -dir <>

      To run several instances it is indispensable to use different SingleFiles as it is not possible to share it, 
      use the "-dir <directory>" option and set different directories for each instance
      If no options are set the cache start with DIST.SYNC mode without passivation and preload. The __defaultCache.dat is 
      stored in the current folder.

