package com.page5of4.dropwizard.discovery.zookeeper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.dropwizard.lifecycle.Managed;
import org.apache.curator.framework.CuratorFramework;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ServiceRegistry implements Managed {
   private static ServiceRegistry singleton;
   private final CuratorFramework curatorClient;
   private final Map<Class<?>, ServicePublisher<?>> services = Maps.newHashMap();
   private final String path;
   private boolean started;

   public static ServiceRegistry get() {
      return singleton;
   }

   public ServiceRegistry(CuratorFramework curatorClient, String path) {
      this.curatorClient = curatorClient;
      this.path = path;
      this.singleton = this;
   }

   @SuppressWarnings("unchecked")
   public <T> Collection<T> getServices(Class<T> klass) {
      List<T> instances = Lists.newArrayList();
      for(Object service : services.get(klass).getServices()) {
         instances.add(klass.cast(service));
      }
      return instances;
   }

   @SuppressWarnings("unchecked")
   public <T> void publish(T service) {
      publish(service.getClass().getName(), service);
   }

   @SuppressWarnings("unchecked")
   public <T> void publish(String name, T service) {
      ServicePublisher<T> publisher = new ServicePublisher<T>(curatorClient, path, name, (Class<T>)service.getClass(), service);
      services.put(service.getClass(), publisher);
      if(started) {
         try {
            publisher.start();
         }
         catch(Exception e) {
            throw new RuntimeException(e);
         }
      }
   }

   @Override
   public void start() throws Exception {
      for(Managed service : services.values()) {
         service.start();
      }
      started = true;
   }

   @Override
   public void stop() throws Exception {
      for(Managed service : services.values()) {
         service.stop();
      }
   }
}
