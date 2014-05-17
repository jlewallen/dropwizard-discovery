package com.page5of4.dropwizard.discovery.zookeeper;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("service")
public class ServiceInstanceRecord {
   private String description;

   public ServiceInstanceRecord() {
      this("");
   }

   public ServiceInstanceRecord(String description) {
      this.description = description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDescription() {
      return description;
   }
}
