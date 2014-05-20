package com.page5of4.dropwizard.discovery.zookeeper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.dropwizard.lifecycle.Managed;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ServiceRegistry implements Managed {
   private static ServiceRegistry singleton;
   private final CuratorFramework curatorClient;
   private final Map<Class<?>, ServiceDiscovery<?>> discoveryCache = Maps.newHashMap();
   private final Map<Object, ServiceDiscovery<?>> publishers = Maps.newHashMap();
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
      for(Object service : getServices(klass, klass.getName())) {
         instances.add(klass.cast(service));
      }
      return instances;
   }

   @SuppressWarnings("unchecked")
   private <T> ServiceDiscovery<T> getServiceDiscovery(Class<T> klass, String name, T self) {
      if(discoveryCache.containsKey(klass)) {
         return (ServiceDiscovery<T>)discoveryCache.get(klass);
      }
      try {
         JsonInstanceSerializer<T> serializer = new JsonInstanceSerializer<T>(klass);
         ServiceInstance<T> selfInstance = self == null ? null : ServiceInstance.<T>builder().name(name).payload(self).port(0).build();
         ServiceDiscovery<T> serviceDiscovery = ServiceDiscoveryBuilder.builder(klass).client(curatorClient).basePath(path).serializer(serializer).thisInstance(selfInstance).build();
         discoveryCache.put(klass, serviceDiscovery);
         if(started) {
            serviceDiscovery.start();
         }
         return serviceDiscovery;
      }
      catch(Exception e) {
         throw new RuntimeException(e);
      }
   }

   private <T> Collection<T> getServices(Class<T> klass, String name) {
      try {
         Collection<T> services = Lists.newArrayList();
         Collection<ServiceInstance<T>> instances = getServiceDiscovery(klass, name, null).queryForInstances(name);
         for(ServiceInstance<T> instance : instances) {
            services.add(instance.getPayload());
         }
         return services;
      }
      catch(Exception e) {
         throw new RuntimeException(e);
      }
   }

   @SuppressWarnings("unchecked")
   public <T> void publish(T service) {
      publish(service.getClass().getName(), service);
   }

   @SuppressWarnings("unchecked")
   public <T> void publish(String name, T service) {
      publishers.put(service, getServiceDiscovery((Class<T>)service.getClass(), name, service));
   }

   @SuppressWarnings("unchecked")
   public <T> void unpublish(T service) {
      if(publishers.containsKey(service)) {
         CloseableUtils.closeQuietly(publishers.get(service));
         publishers.remove(service);
      }
   }

   @Override
   public void start() throws Exception {
      for(ServiceDiscovery<?> serviceDiscovery : discoveryCache.values()) {
         serviceDiscovery.start();
      }
      started = true;
   }

   @Override
   public void stop() throws Exception {
      for(Closeable serviceDiscovery : discoveryCache.values()) {
         CloseableUtils.closeQuietly(serviceDiscovery);
      }
   }
}
