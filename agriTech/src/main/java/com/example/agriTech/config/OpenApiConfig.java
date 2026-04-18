package com.example.agriTech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("hệ thống nông nghiệp")
                    .version("1.0")
                    .description("hệ thông quản lý giám sát cây trồng")
                    .contact(new Contact().name("Bảo Luân").email("luanhpb.24ite@vku.udn.vn"))

                );
    }
}