package com.page5of4.dropwizard.discovery.zookeeper;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.curator.framework.CuratorFramework;

public class ZooKeeperBundle implements ConfiguredBundle<ConfiguresZooKeeper> {
   private boolean startServer;

   public ZooKeeperBundle(boolean startServer) {
      this.startServer = startServer;
   }

   @Override
   public void initialize(Bootstrap<?> bootstrap) {
   }

   @Override
   public void run(ConfiguresZooKeeper configuration, Environment environment) {
      if(startServer) {
         environment.lifecycle().manage(new ManagedZooKeeperServer());
      }
      CuratorFramework curator = configuration.getZooKeeperConfiguration().getCurator();
      ManagedCurator managedCurator = new ManagedCurator(curator);
      ServiceRegistry serviceRegistry = new ServiceRegistry(curator, configuration.getZooKeeperConfiguration().getServicesPath());

      environment.lifecycle().manage(managedCurator);
      environment.lifecycle().manage(serviceRegistry);
   }
}
