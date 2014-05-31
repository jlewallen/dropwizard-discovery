package com.page5of4.dropwizard.discovery.zookeeper;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.curator.framework.CuratorFramework;

public class ZooKeeperBundle implements ConfiguredBundle<ConfiguresZooKeeper> {
   @Override
   public void initialize(Bootstrap<?> bootstrap) {
   }

   @Override
   public void run(ConfiguresZooKeeper configuration, Environment environment) {
      if(!configuration.getZooKeeper().getEnabled()) {
         return;
      }
      if(configuration.getZooKeeper().getServer()) {
         environment.lifecycle().manage(new ManagedZooKeeperServer());
      }
      CuratorFramework curator = configuration.getZooKeeper().getCurator();
      ManagedCurator managedCurator = new ManagedCurator(curator);
      ServiceRegistry serviceRegistry = new ServiceRegistry(curator, configuration.getZooKeeper().getServicesPath());

      environment.lifecycle().manage(managedCurator);
      environment.lifecycle().manage(serviceRegistry);
      environment.healthChecks().register("zookeeper", new ZooKeeperHealthCheck(curator));
   }
}
