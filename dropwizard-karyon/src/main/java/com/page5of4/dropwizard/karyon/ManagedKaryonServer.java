package com.page5of4.dropwizard.karyon;

import com.netflix.karyon.server.KaryonServer;
import com.page5of4.dropwizard.ConfiguresEurekaClient;
import com.page5of4.dropwizard.EurekaClientConfiguration;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.server.ServerFactory;

public class ManagedKaryonServer implements Managed {
   private final EurekaClientConfiguration eurekaClientConfiguration;
   private final ServerFactory serverFactory;
   private KaryonServer server;

   public ManagedKaryonServer(ConfiguresEurekaClient configuresEurekaClient) {
      this.eurekaClientConfiguration = configuresEurekaClient.getEureka();
      this.serverFactory = configuresEurekaClient.getServerFactory();
   }

   @Override
   public void start() throws Exception {
      server = new KaryonServer();
      server.initialize();
      server.start();
   }

   @Override
   public void stop() throws Exception {
      server.close();
   }
}
