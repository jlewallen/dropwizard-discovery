package com.page5of4.dropwizard.karyon;

import com.page5of4.dropwizard.ConfiguresEurekaClient;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class KaryonBundle implements ConfiguredBundle<ConfiguresEurekaClient> {
   @Override
   public void run(ConfiguresEurekaClient configuration, Environment environment) throws Exception {
      environment.lifecycle().manage(new ManagedKaryonServer(configuration));
   }

   @Override
   public void initialize(Bootstrap<?> bootstrap) {

   }
}
