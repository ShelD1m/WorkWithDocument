package com.dmitry.document.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "rules")
public class RulesProperties {
    /** classpath:rules.yaml или file:/path/to/rules.yaml */
    @NotBlank
    private String location;

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
