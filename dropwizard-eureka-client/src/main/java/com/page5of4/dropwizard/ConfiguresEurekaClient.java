package com.page5of4.dropwizard;

import io.dropwizard.server.ServerFactory;

public interface ConfiguresEurekaClient {
   ServerFactory getServerFactory();

   EurekaClientConfiguration getEureka();
}
