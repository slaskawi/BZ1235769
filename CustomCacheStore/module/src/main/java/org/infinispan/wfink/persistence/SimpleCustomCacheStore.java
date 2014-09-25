package org.infinispan.wfink.persistence;

import java.util.concurrent.Executor;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.InitializationContext;
import org.jboss.logging.Logger;

@ConfiguredBy(SimpleCustomCacheStoreConfiguration.class)
public class SimpleCustomCacheStore implements AdvancedLoadWriteStore<Object, Object> {
   private static final Logger LOGGER = Logger.getLogger(SimpleCustomCacheStore.class);

   @Override
   public boolean contains(Object arg0) {
      //FIXME implement me
      LOGGER.fatal("contains(" + arg0 + ")");
      return false;
   }

   @Override
   public void init(InitializationContext arg0) {
      //FIXME implement me
      LOGGER.fatal("init(" + arg0 + ")");
   }

   @Override
   public MarshalledEntry<Object, Object> load(Object arg0) {
      //FIXME implement me
      LOGGER.fatal("load(" + arg0 + ")");
      return null;
   }

   @Override
   public void start() {
      //FIXME implement me
      LOGGER.fatal("start()");
   }

   @Override
   public void stop() {
      //FIXME implement me
      LOGGER.fatal("stop()");
   }

   @Override
   public boolean delete(Object arg0) {
      //FIXME implement me
      LOGGER.fatal("delete(" + arg0 + ") " + arg0.getClass().getName());
      return false;
   }

   @Override
   public void write(MarshalledEntry<Object, Object> arg0) {
      //FIXME implement me
      LOGGER.fatal("write(" + arg0 + ")");
   }

   @Override
   public void process(org.infinispan.persistence.spi.AdvancedCacheLoader.KeyFilter<Object> filter,
         org.infinispan.persistence.spi.AdvancedCacheLoader.CacheLoaderTask<Object, Object> task, Executor executor, boolean fetchValue,
         boolean fetchMetadata) {
      //FIXME implement me
      LOGGER.fatal("process(" + filter + ", " + task + ", " + executor + ", fetchValue=" + fetchValue + ", fetchMeta=" + fetchMetadata + ")");
   }

   @Override
   public int size() {
      //FIXME implement me
      LOGGER.fatal("size()");
      return 0;
   }

   @Override
   public void clear() {
      //FIXME implement me
      LOGGER.fatal("clear()");
   }

   @Override
   public void purge(Executor arg0, org.infinispan.persistence.spi.AdvancedCacheWriter.PurgeListener arg1) {
      //FIXME implement me
      LOGGER.fatal("purge(" + arg0 + "," + arg1 + ")");
   }

}
