package com.page5of4.dropwizard.discovery.zookeeper;

import io.dropwizard.lifecycle.Managed;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;

import java.io.File;

public class ManagedZooKeeperServer implements Managed {
   private ZooKeeperServerMain zk;
   private ServerCnxnFactory cnxnFactory;
   private ZooKeeperServer zkServer;

   @Override
   public void start() throws Exception {
      ServerConfig config = new ServerConfig();
      config.parse(new String[] { "2181", "zk" });

      zkServer = new ZooKeeperServer();
      zkServer.setTxnLogFactory(new FileTxnSnapLog(new File(config.getDataLogDir()), new File(config.getDataDir())));
      zkServer.setTickTime(config.getTickTime());
      zkServer.setMinSessionTimeout(config.getMinSessionTimeout());
      zkServer.setMaxSessionTimeout(config.getMaxSessionTimeout());
      cnxnFactory = ServerCnxnFactory.createFactory();
      cnxnFactory.configure(config.getClientPortAddress(), config.getMaxClientCnxns());
      cnxnFactory.startup(zkServer);
   }

   @Override
   public void stop() throws Exception {
      cnxnFactory.shutdown();
      cnxnFactory.join();
      if(zkServer.isRunning()) {
         zkServer.shutdown();
      }
   }
}
