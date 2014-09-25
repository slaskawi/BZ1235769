package org.infinispan.wfink.persistence;

import java.util.Properties;

import org.infinispan.commons.configuration.BuiltBy;
import org.infinispan.commons.configuration.ConfigurationFor;
import org.infinispan.configuration.cache.AbstractStoreConfiguration;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;
import org.infinispan.configuration.cache.SingletonStoreConfiguration;
import org.jboss.logging.Logger;

@ConfigurationFor(SimpleCustomCacheStore.class)
@BuiltBy(SimpleCustomCacheStoreConfigurationBuilder.class)
public class SimpleCustomCacheStoreConfiguration extends AbstractStoreConfiguration {
   private static final Logger LOGGER = Logger.getLogger(SimpleCustomCacheStoreConfiguration.class);

   public SimpleCustomCacheStoreConfiguration(boolean purgeOnStartup, boolean fetchPersistentState, boolean ignoreModifications,
         AsyncStoreConfiguration async, SingletonStoreConfiguration singletonStore, boolean preload, boolean shared, Properties properties) {
      super(purgeOnStartup, fetchPersistentState, ignoreModifications, async, singletonStore, preload, shared, properties);
      LOGGER.fatal("Construct (purge" + purgeOnStartup + ", fetchP=" + fetchPersistentState + ", ignoreMod=" + ignoreModifications + ", " + async
            + ", " + singletonStore + ", preload=" + preload + ", shared=" + shared + ", PROP=" + properties + ")");
   }

   public String resolvePath() {
      LOGGER.fatal("resolvePath() properties=" + properties());
      return properties().getProperty("path");
   }
}
