package com.capstone.subway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI subwayOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Daegu Subway API")
                        .description("Backend API for the Daegu subway (도시철도) app: lines, stations, routes, timetables and get-off alarms.")
                        .version("v1"));
    }
}
