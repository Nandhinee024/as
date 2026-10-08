package com.smartassessment.assessment.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI assessmentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Assessment Platform - Assessment Service API")
                        .description("REST API documentation for Assessment Service managing examination configurations, sections, proctoring rules, enrollments, and candidate access verification.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Smart Assessment Platform Team")
                                .email("backend-team@smartassessment.internal"))
                        .license(new License().name("Internal Proprietary").url("https://smartassessment.internal")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT Bearer token format: Bearer <token>")));
    }
}
