package com.page5of4.dropwizard.discovery.zookeeper;

import com.google.common.collect.Lists;
import io.dropwizard.lifecycle.Managed;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;

public class ServiceRegistry implements Managed {
   private static ServiceRegistry singleton;
   private final CuratorFramework curatorClient;
   private final List<Managed> services = Lists.newArrayList();
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
   public <T> void publish(String name, T service) {
      ServicePublisher<T> publisher = new ServicePublisher<T>(curatorClient, path, name, (Class<T>)service.getClass(), service);
      services.add(publisher);
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
      for(Managed service : services) {
         service.start();
      }
      started = true;
   }

   @Override
   public void stop() throws Exception {
      for(Managed service : services) {
         service.stop();
      }
   }
}
