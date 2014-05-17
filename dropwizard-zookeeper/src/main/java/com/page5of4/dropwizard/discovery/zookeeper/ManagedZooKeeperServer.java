package com.page5of4.dropwizard.discovery.zookeeper;

import io.dropwizard.lifecycle.Managed;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;

public class ManagedZooKeeperServer implements Managed {
   private ZooKeeperServerMain zk;

   @Override
   public void start() throws Exception {
      ServerConfig config = new ServerConfig();
      config.parse(new String[] { "2181", "zk" });
      zk = new ZooKeeperServerMain();
      zk.runFromConfig(config);
   }

   @Override
   public void stop() throws Exception {
   }
}
