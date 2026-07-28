package com.ecommerce.authservice.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI OpenAPI(){

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Authentication Service APIS")
                                .version("1.0")
                                .description("Authentication Service APIS"));

    }
}
