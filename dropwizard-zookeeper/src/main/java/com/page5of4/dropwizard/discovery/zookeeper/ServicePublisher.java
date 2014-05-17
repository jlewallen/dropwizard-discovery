package com.page5of4.dropwizard.discovery.zookeeper;

import com.google.common.collect.Lists;
import io.dropwizard.lifecycle.Managed;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;

public class ServicePublisher<T> implements Managed {
   private final ServiceDiscovery<T> serviceDiscovery;
   private final ServiceInstance<T> self;
   private String path;
   private String name;
   private Class<T> klass;

   public Collection<T> getServices() {
      try {
         Collection<T> services = Lists.newArrayList();
         Collection<ServiceInstance<T>> instances = serviceDiscovery.queryForInstances(this.name);
         for(ServiceInstance<T> instance : instances) {
            services.add(instance.getPayload());
         }
         return services;
      }
      catch(Exception e) {
         throw new RuntimeException(e);
      }
   }

   public ServicePublisher(CuratorFramework curatorClient, String path, String name, Class<T> klass, T service) {
      try {
         JsonInstanceSerializer<T> serializer = new JsonInstanceSerializer<T>(klass);
         this.path = path;
         this.name = name;
         this.klass = klass;
         this.self = ServiceInstance.<T>builder().name(name).payload(service).port((int)(65535 * Math.random())).build();
         this.serviceDiscovery = ServiceDiscoveryBuilder.builder(klass).client(curatorClient).basePath(path).serializer(serializer).thisInstance(self).build();
      }
      catch(Exception e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void start() throws Exception {
      serviceDiscovery.start();
   }

   @Override
   public void stop() throws Exception {
      CloseableUtils.closeQuietly(serviceDiscovery);
   }
}
