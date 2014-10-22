package org.infinispan.wfink.hotrod.embedded;

import java.util.HashMap;
import java.util.Map;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class HotRodClient {

   private RemoteCacheManager remoteCacheManager;
   private RemoteCache<String, String> remoteCache;

   public HotRodClient(String host, String port) {
      ConfigurationBuilder remoteBuilder = new ConfigurationBuilder();
      remoteBuilder.addServer().host(host).port(Integer.parseInt(port));
      remoteCacheManager = new RemoteCacheManager(remoteBuilder.build());
      remoteCache = remoteCacheManager.getCache();
   }

   private void insert(Map<String, String> values) {
      System.out.println("Inserting data into cache...");
      for (Map.Entry<String, String> entry : values.entrySet()) {
         System.out.println(entry.getKey() + " -> " + entry.getValue());
         remoteCache.put(entry.getKey(), entry.getValue());
      }
   }

   private void verify(Map<String, String> values) {
      System.out.println("\nVerifying data...");

      for (Map.Entry<String, String> entry : values.entrySet()) {
         System.out.print("verify key " + entry.getKey());
         String value = remoteCache.get(entry.getKey());
         if (value == null) {
            System.out.println(" No value found!");
         } else if (!value.equals(entry.getValue())) {
            System.out.println(" Value '" + value + "' differ from '" + entry.getValue() + "'");
         } else {
            System.out.println(" ok");
         }
      }
   }

   private void stop() {
      remoteCacheManager.stop();
   }

   public static void main(String[] args) {
      String host = "localhost";
      String port = "11111";

      if (args.length > 0) {
         port = args[0];
      }
      if (args.length > 1) {
         port = args[1];
      }
      HotRodClient client = new HotRodClient(host, port);

      HashMap<String, String> values = new HashMap<String, String>();
      values.put("one", "First Value");
      values.put("two", "Second Value");
      values.put("three", "Third Value");
      values.put("four", "Fourth Value");
      values.put("five", "Fifth Value");
      values.put("six", "Sixth Value");
      values.put("seven", "Seventh Value");
      client.insert(values);
      client.verify(values);
      client.stop();
      System.out.println("\nDone !");
   }

}
