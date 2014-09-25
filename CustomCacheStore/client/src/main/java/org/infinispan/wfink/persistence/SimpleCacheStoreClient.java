/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.infinispan.wfink.persistence;

import java.io.Console;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

/**
 * @author "Wolf-Dieter Fink"
 */
public class SimpleCacheStoreClient {

   private static final String JDG_HOST = "server.host";
   private static final String HOTROD_PORT = "server.hotrod.port";
   private static final String CACHE_NAME = "server.cache.name";
   private static final String PROPERTIES_FILE = "server.properties";
   private static final String msgKeyNotFound = "No entry with key \"%s\" found\n";
   private static final String msgEnterKey = "Enter key: ";
   private static final String initialPrompt = "Choose action:\n" + "============= \n" + "a  -  add a entry\n" + "g  -  get an entry\n"
         + "r  -  remove a entry\n" + "l   -  list all entries\n" + "q   -  quit\n";

   private Console con;
   private RemoteCacheManager cacheManager;
   private RemoteCache<String, String> cache;

   public SimpleCacheStoreClient(Console con) {
      this.con = con;
      ConfigurationBuilder builder = new ConfigurationBuilder();
      builder.addServer().host(jdgProperty(JDG_HOST)).port(Integer.parseInt(jdgProperty(HOTROD_PORT)));
      cacheManager = new RemoteCacheManager(builder.build());
      cache = cacheManager.getCache(jdgProperty(CACHE_NAME));
   }

   public void add() {
      String key = con.readLine(msgEnterKey);
      String value = con.readLine("Enter value: ");

      cache.put(key, value);
   }

   public void get() {
      String key = con.readLine(msgEnterKey);

      if (cache.containsKey(key)) {
         con.printf("value : %s\n", cache.get(key));
      } else {
         con.printf(msgKeyNotFound, key);
      }
   }

   public void size() {
      con.printf("cache has %d entries\n", cache.size());
   }

   public void remove() {
      String key = con.readLine(msgEnterKey);

      if (cache.containsKey(key)) {
         cache.remove(key);
      } else {
         con.printf(msgKeyNotFound, key);
      }
   }

   public void list() {
      for (String key : cache.keySet()) {
         con.printf("%s  : %s\n", key, cache.get(key));
      }
   }

   public void stop() {
      cacheManager.stop();
   }

   public static void main(String[] args) {
      Console con = System.console();
      SimpleCacheStoreClient manager = new SimpleCacheStoreClient(con);
      con.printf(initialPrompt);

      while (true) {
         String action = con.readLine(">");
         if ("a".equals(action)) {
            manager.add();
         } else if ("g".equals(action)) {
            manager.get();
         } else if ("r".equals(action)) {
            manager.remove();
         } else if ("s".equals(action)) {
            manager.size();
         } else if ("l".equals(action)) {
            manager.list();
         } else if ("q".equals(action)) {
            manager.stop();
            break;
         }
      }
   }

   public static String jdgProperty(String name) {
      Properties props = new Properties();
      try {
         props.load(SimpleCacheStoreClient.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
      } catch (IOException ioe) {
         throw new RuntimeException(ioe);
      }
      System.out.println(name + ":" + props.getProperty(name) + "<");
      return props.getProperty(name);
   }
}
