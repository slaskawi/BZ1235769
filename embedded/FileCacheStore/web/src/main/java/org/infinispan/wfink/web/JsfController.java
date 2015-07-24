/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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
package org.infinispan.wfink.web;

import org.infinispan.Cache;
import org.infinispan.context.Flag;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A simple JSF controller to show how the EJB invocation on different servers.
 * 
 * @author <a href="mailto:wfink@redhat.com">Wolf-Dieter Fink</a>
 */
@Model
public class JsfController {
   private static final Logger LOGGER = Logger.getLogger(JsfController.class);
   private CacheView cacheView;
   
   @Inject
   private EmbeddedCacheManager manager;

   /**
    * Initialize the controller.
    */
   @PostConstruct
   public void init() {
      LOGGER.info("CacheManager : custer=" + manager.getClusterName());
      initForm();
   }
   
   private Cache<String, String> getEmbeddedCache() {
      return manager.getCache("FileCacheStore");
   }

   public void initForm() {
      this.cacheView = new CacheView();
   }

   @Produces
   @Named
   public CacheView getCache() {
      return this.cacheView;
   }

   public void add() {
      getEmbeddedCache().put(cacheView.getKey(), cacheView.getValue());
      getSize();
   }

   public void get() {
      String key = cacheView.getKey();
      LOGGER.info("Try to read key="+key);
      String value = getEmbeddedCache().get(key);
      if(value==null) {
         cacheView.setValue("NOT AVAILABLE");
      }else{
         cacheView.setValue(value);
      }
   }

   public void delete() {
      getEmbeddedCache().remove(cacheView.getKey());
      getSize();
   }
   
   public void getSize() {
      cacheView.setSize(getEmbeddedCache().size());
      cacheView.setSizeM(getEmbeddedCache().getAdvancedCache().withFlags(Flag.SKIP_CACHE_LOAD).size());
   }

   public void list() {
      StringBuilder result = new StringBuilder();
      // read cache
      Cache<String, String> cache = getEmbeddedCache();
      for (String key : cache.keySet()) {
         result.append(key + "  :  ");
         result.append(cache.get(key));
         result.append(" || ");
      }
      cacheView.setEntries(result.toString());
      getSize();
   }
}
