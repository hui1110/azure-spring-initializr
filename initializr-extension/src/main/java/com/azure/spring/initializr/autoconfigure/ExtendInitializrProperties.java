package com.azure.spring.initializr.autoconfigure;

import com.azure.spring.initializr.metadata.Architecture;
import com.azure.spring.initializr.metadata.connector.Connector;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "extend.initializr")
public class ExtendInitializrProperties {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExtendInitializrProperties.class);


    @JsonIgnore
//    @NestedConfigurationProperty
    private final List<Architecture> architectures = new ArrayList<>();


    @JsonIgnore
    private final Map<String, Connector> connectors = new HashMap();

    public List<Architecture> getArchitectures() {
        return architectures;
    }

    public Map<String, Connector> getConnectors() {
        return connectors;
    }

}
