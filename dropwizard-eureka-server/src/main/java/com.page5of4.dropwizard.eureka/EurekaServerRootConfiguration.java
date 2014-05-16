package com.page5of4.dropwizard.eureka;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class EurekaServerRootConfiguration extends Configuration {
   @Valid
   @NotNull
   @JsonProperty
   private final EurekaServerConfiguration eureka = new EurekaServerConfiguration();

   public EurekaServerConfiguration getEureka() {
      return eureka;
   }
}
