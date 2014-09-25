package org.infinispan.wfink.persistence;

import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.jboss.logging.Logger;

public class SimpleCustomCacheStoreConfigurationBuilder extends
      AbstractStoreConfigurationBuilder<SimpleCustomCacheStoreConfiguration, SimpleCustomCacheStoreConfigurationBuilder> {
   private static final Logger LOGGER = Logger.getLogger(SimpleCustomCacheStoreConfigurationBuilder.class);

   public SimpleCustomCacheStoreConfigurationBuilder(final PersistenceConfigurationBuilder builder) {
      super(builder);
      LOGGER.fatal("construct");
   }

   @Override
   public SimpleCustomCacheStoreConfiguration create() {
      LOGGER.fatal("create");
      return new SimpleCustomCacheStoreConfiguration(purgeOnStartup, fetchPersistentState, ignoreModifications, async.create(),
            singletonStore.create(), preload, shared, properties);
   }

   @Override
   public SimpleCustomCacheStoreConfigurationBuilder self() {
      LOGGER.fatal("self");
      return this;
   }

   @Override
   public SimpleCustomCacheStoreConfigurationBuilder read(SimpleCustomCacheStoreConfiguration template) {
      LOGGER.fatal("read");
      super.read(template);
      return this;
   }

}
