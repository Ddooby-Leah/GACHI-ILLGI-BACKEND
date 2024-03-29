package com.ddooby.gachiillgi.base.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {@Server(url = "http://localhost:8080"),
                @Server(url = "http://ec2-3-36-55-16.ap-northeast-2.compute.amazonaws.com:8080")},

        info = @Info(
                title = "가치일기 API",
                description = "가치 읽을래?",
                version = "v1"))

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi nonSecurityGroupOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("Gachi Diary Open API Group")
                .pathsToMatch("/**")
                .build();
    }
}
