package com.page5of4.dropwizard.discovery.zookeeper;

import io.dropwizard.lifecycle.Managed;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

public class ManagedCurator implements Managed {
   private final CuratorFramework curatorClient;

   public CuratorFramework getCuratorClient() {
      return curatorClient;
   }

   public ManagedCurator(String connectString) {
      curatorClient = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
   }

   @Override
   public void start() throws Exception {
      curatorClient.start();
   }

   @Override
   public void stop() throws Exception {
      CloseableUtils.closeQuietly(curatorClient);
   }
}
