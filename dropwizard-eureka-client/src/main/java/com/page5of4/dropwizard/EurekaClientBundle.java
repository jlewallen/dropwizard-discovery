package com.page5of4.dropwizard;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class EurekaClientBundle implements ConfiguredBundle<ConfiguresEurekaClient> {
   @Override
   public void run(ConfiguresEurekaClient configuration, Environment environment) throws Exception {
      EurekaClientHealthCheck healthCheck = new EurekaClientHealthCheck();
      EurekaInstance eurekaInstance = new EurekaInstance(configuration, healthCheck);
      environment.lifecycle().manage(eurekaInstance);
      environment.healthChecks().register("eureka", healthCheck);

      environment.admin().addTask(new UpTask(eurekaInstance));
      environment.admin().addTask(new DownTask(eurekaInstance));
   }

   @Override
   public void initialize(Bootstrap<?> bootstrap) {
   }

}
