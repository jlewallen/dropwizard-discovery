package com.page5of4.dropwizard;

import com.netflix.appinfo.PropertiesInstanceConfig;
import com.netflix.config.DynamicPropertyFactory;
import com.sun.jersey.spi.resource.Singleton;

/**
 * An InstanceConfig that makes running multiple services on a single machine easier. Easy machine is
 * given a unique publicly available hostname via the xip.io service from 37signals by combining the vipAddress
 * and the ipAddress.
 */
@Singleton
public class DeveloperMachineDataCenterInstanceConfig extends PropertiesInstanceConfig {
   private static final DynamicPropertyFactory INSTANCE = com.netflix.config.DynamicPropertyFactory.getInstance();

   public DeveloperMachineDataCenterInstanceConfig(String namespace) {
      super(namespace);
   }

   @Override
   public String getVirtualHostName() {
      return INSTANCE.getStringProperty(namespace + "vipAddress", null).get();
   }

   @Override
   public String getHostName(boolean refresh) {
      return getVirtualHostName() + "." + super.getIpAddress() + ".xip.io";
   }
}
