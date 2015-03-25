package org.infinispan.wfink.hotrod.embedded;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;

/**
 * 
 * Simple HotRod server to use embedded in a java application.
 * To use this for JDG a <b>Support Exception</b> is necessary!
 * 
 * @author Wolf-Dieter Fink
 */
public class SimpleHotRodServer {

   private DefaultCacheManager defaultCacheManager;
   private HotRodServer server;
   private HotRodServerConfiguration build;

   public SimpleHotRodServer(String host, int port) {
      ConfigurationBuilder embeddedBuilder = new ConfigurationBuilder();
      // Equivalence is needed to compare the server side byte arrays
      embeddedBuilder.dataContainer().keyEquivalence(new AnyServerEquivalence()).valueEquivalence(new AnyServerEquivalence());
      defaultCacheManager = new DefaultCacheManager(embeddedBuilder.build());

      build = new HotRodServerConfigurationBuilder().host(host).port(port).name("XX").build();
      server = new HotRodServer();
   }

   public void start() {
      server.start(build, defaultCacheManager);
   }

   public void stop() {
      server.stop();
      defaultCacheManager.stop();
   }
}
