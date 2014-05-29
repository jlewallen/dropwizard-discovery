package com.page5of4.dropwizard.discovery.zookeeper;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZooKeeperConfiguration {
   @JsonProperty
   private boolean server;

   @JsonProperty
   private String address = "127.0.0.1:2181";

   @JsonProperty
   private String servicesPath = "/services";
   private CuratorFramework curator;

   public boolean getServer() {
      return server;
   }

   public void setServer(boolean server) {
      this.server = server;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getServicesPath() {
      return servicesPath;
   }

   public void setServicesPath(String servicesPath) {
      this.servicesPath = servicesPath;
   }

   public CuratorFramework getCurator() {
      if(curator == null) {
         curator = CuratorFrameworkFactory.newClient(getAddress(), new ExponentialBackoffRetry(1000, 3));
      }
      return curator;
   }
}
