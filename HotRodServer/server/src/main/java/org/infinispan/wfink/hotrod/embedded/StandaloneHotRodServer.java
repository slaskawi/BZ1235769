
 package org.infinispan.wfink.hotrod.embedded;

import sun.misc.Signal;
import sun.misc.SignalHandler;

@SuppressWarnings("restriction")
public class StandaloneHotRodServer implements SignalHandler {
   private final SimpleHotRodServer hotRodServer;
   private boolean stopRequest = false;
   
   public StandaloneHotRodServer(String host, int port) {
      hotRodServer = new SimpleHotRodServer(host, port);
      setSignalHandler(this);
      hotRodServer.start();
   }

   /**
    * Set handler for TERM INT and HUP(if available)
    *
    * @param handler
    *            the signal handler
    */
   private static void setSignalHandler(SignalHandler handler) {
     Signal.handle(new Signal("TERM"), handler);
     Signal.handle(new Signal("INT"), handler);
     Signal.handle(new Signal("USR2"), handler);
     try {
       Signal.handle(new Signal("HUP"), handler);
     } catch (IllegalArgumentException e) {
       // ignore HUP if signal not available (Windows)
     }
   }

   /**
    * Here we handle the Signals.
    */
   @Override
   public void handle(Signal signal) {
     System.out.println("Receive signal " + signal + " try to stop processing");
     stopRequest = true;
   }


   private void waitUntilStopRequested() {
      while( !stopRequest ) {
         try {
            Thread.sleep(2000);
         } catch (InterruptedException e) {}
      }
      
      hotRodServer.stop();
   }

   public static void main(String[] args) {
      String host = "localhost";
      int port = 11111;

      if (args.length > 0) {
         port = Integer.parseInt(args[0]);
      }
      if (args.length > 1) {
         
         host = args[1];
      }

      StandaloneHotRodServer server = new StandaloneHotRodServer(host, port);
      server.waitUntilStopRequested();
      System.out.println("HotRod server stopped");
   }
}
