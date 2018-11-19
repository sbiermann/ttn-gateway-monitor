package com.ems.ttngwmonitor.util;

import org.apache.deltaspike.core.api.config.PropertyFileConfig;


public class PropertyLoader implements PropertyFileConfig {
    @Override
    public String getPropertyFileName() {
        return "application.properties";
    }

    @Override
    public boolean isOptional() {
        return false;
    }
}
