package com.page5of4.dropwizard.discovery.zookeeper;

import io.dropwizard.lifecycle.Managed;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagedCurator implements Managed {
   private static final Logger logger = LoggerFactory.getLogger(ManagedCurator.class);
   private final CuratorFramework curatorClient;

   public CuratorFramework getCuratorClient() {
      return curatorClient;
   }

   public ManagedCurator(String connectString) {
      curatorClient = CuratorFrameworkFactory.newClient(connectString, new ExponentialBackoffRetry(1000, 3));
   }

   @Override
   public void start() throws Exception {
      logger.info("Starting Curator");
      curatorClient.start();
   }

   @Override
   public void stop() throws Exception {
      CloseableUtils.closeQuietly(curatorClient);
   }
}
