package com.dmitry.document.config;

import com.dmitry.document.config.RulesProperties;

import com.dmitry.document.engine.RulesConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableConfigurationProperties(RulesProperties.class)
public class AppConfig {

    @Bean
    public RulesConfig rulesConfig(RulesProperties props, ResourceLoader rl) throws Exception {
        Resource r = rl.getResource(props.getLocation());
        try (var in = r.getInputStream()) {
            return RulesConfig.fromYaml(in);
        }
    }
}
