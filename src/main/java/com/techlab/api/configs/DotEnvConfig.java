package com.techlab.api.configs;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnvConfig {
    static {
        Dotenv
            .configure()
            .directory(".")
            .ignoreIfMissing()
            .systemProperties()
            .load();
    }
}
