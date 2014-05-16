package com.page5of4.dropwizard.eureka;

import com.codahale.metrics.JmxReporter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.provider.DiscoveryJerseyProvider;
import com.netflix.eureka.EurekaBootStrap;
import com.netflix.eureka.resources.ASGResource;
import com.netflix.eureka.resources.ApplicationsResource;
import com.netflix.eureka.resources.InstancesResource;
import com.netflix.eureka.resources.PeerReplicationResource;
import com.netflix.eureka.resources.SecureVIPResource;
import com.netflix.eureka.resources.StatusResource;
import com.netflix.eureka.resources.VIPResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.configuration.BaseConfiguration;

public class Main extends Application<EurekaServerRootConfiguration> {
   public static void main(String[] args) throws Exception {
      new Main().run(new String[] { "server", System.getProperty("dropwizard.config") });
   }

   @Override
   public void initialize(Bootstrap<EurekaServerRootConfiguration> bootstrap) {
   }

   @Override
   public void run(EurekaServerRootConfiguration configuration, Environment environment) throws ClassNotFoundException {
      JmxReporter.forRegistry(environment.metrics()).build().start();

      BaseConfiguration baseConfiguration = new BaseConfiguration();
      baseConfiguration.setProperty("eureka.waitTimeInMsWhenSyncEmpty", configuration.getEureka().getWaitTimeInMsWhenSyncEmpty());
      baseConfiguration.setProperty("eureka.numberRegistrySyncRetries", configuration.getEureka().getNumberRegistrySyncRetries());
      baseConfiguration.setProperty("eureka.serviceUrl.default", configuration.getEureka().getDefaultServiceUrl());
      baseConfiguration.setProperty("eureka.shouldFetchRegistry", false);
      ConfigurationManager.loadPropertiesFromConfiguration(baseConfiguration);

      environment.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      environment.getObjectMapper().configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

      environment.servlets().addServletListeners(new EurekaBootStrap());

      environment.healthChecks().register("eureka", new EurekaServerHealthCheck());

      environment.jersey().register(ApplicationsResource.class);
      environment.jersey().register(InstancesResource.class);
      environment.jersey().register(PeerReplicationResource.class);
      environment.jersey().register(SecureVIPResource.class);
      environment.jersey().register(StatusResource.class);
      environment.jersey().register(VIPResource.class);
      environment.jersey().register(ASGResource.class);
      environment.jersey().register(DiscoveryJerseyProvider.class);

      environment.jersey().getResourceConfig().getSingletons().clear();
      environment.jersey().getResourceConfig().getSingletons().add(new DiscoveryJerseyProvider());
   }
}

