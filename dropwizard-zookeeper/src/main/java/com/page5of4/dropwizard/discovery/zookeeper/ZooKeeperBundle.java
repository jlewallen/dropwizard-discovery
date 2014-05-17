package com.page5of4.dropwizard.discovery.zookeeper;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ZooKeeperBundle implements Bundle {
   private boolean startServer;
   private String connectString;

   public ZooKeeperBundle(boolean startServer, String connectString) {
      this.startServer = startServer;
      this.connectString = connectString;
   }

   @Override
   public void initialize(Bootstrap<?> bootstrap) {
   }

   @Override
   public void run(Environment environment) {
      if(startServer) {
         environment.lifecycle().manage(new ManagedZooKeeperServer());
      }
      ManagedCurator managedCurator = new ManagedCurator(connectString);
      ServiceRegistry serviceRegistry = new ServiceRegistry(managedCurator.getCuratorClient(), "/services/");

      environment.lifecycle().manage(managedCurator);
      environment.lifecycle().manage(serviceRegistry);
   }
}
