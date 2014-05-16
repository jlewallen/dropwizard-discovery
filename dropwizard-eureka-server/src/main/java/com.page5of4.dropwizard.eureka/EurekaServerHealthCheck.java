package com.page5of4.dropwizard.eureka;

import com.codahale.metrics.health.HealthCheck;

public class EurekaServerHealthCheck extends HealthCheck {
   @Override
   protected Result check() throws Exception {
      return Result.healthy();
   }
}
