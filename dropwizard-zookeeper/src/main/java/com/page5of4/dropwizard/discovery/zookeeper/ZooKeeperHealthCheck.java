package com.page5of4.dropwizard.discovery.zookeeper;

import com.codahale.metrics.health.HealthCheck;
import org.apache.curator.framework.CuratorFramework;

public class ZooKeeperHealthCheck extends HealthCheck {
   private final CuratorFramework curator;

   public ZooKeeperHealthCheck(CuratorFramework curator) {
      this.curator = curator;
   }

   @Override
   protected Result check() throws Exception {
      if(curator.getZookeeperClient().isConnected()) {
         return Result.healthy();
      }
      return Result.unhealthy("Disconnected");
   }
}
