package com.page5of4.dropwizard.discovery;

import java.util.Map;

public interface DiscoveryMetadataProvider {
   Map<String, String> getMetadata();
}
