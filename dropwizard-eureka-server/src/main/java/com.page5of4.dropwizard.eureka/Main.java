package com.page5of4.dropwizard.eureka;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.databind.DeserializationFeature;
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

public class Main extends Application<EurekaServerConfiguration> {
   public static void main(String[] args) throws Exception {
      new Main().run(new String[] { "server", System.getProperty("dropwizard.config") });
   }

   @Override
   public void initialize(Bootstrap<EurekaServerConfiguration> bootstrap) {
   }

   @Override
   public void run(EurekaServerConfiguration configuration, Environment environment) throws ClassNotFoundException {
      JmxReporter.forRegistry(environment.metrics()).build().start();

      System.setProperty("eureka.waitTimeInMsWhenSyncEmpty", "0");
      System.setProperty("eureka.numberRegistrySyncRetries", "0");
      System.setProperty("eureka.serviceUrl.default", "http://127.0.0.1:90/eureka/v2/");

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

   public static class EurekaServerHealthCheck extends HealthCheck {
      @Override
      protected Result check() throws Exception {
         return Result.healthy();
      }
   }
}

