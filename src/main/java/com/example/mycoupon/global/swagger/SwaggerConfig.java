package com.example.mycoupon.global.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI publicApi() {
        String jwtSchemeName = "JWT TOKEN";

        // SecuritySchemes 등록
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        Info apiInfo = new Info()
                .title("mycoupon swaggerUI")
                .description("API 명세서");

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(apiInfo)
                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName))
                .components(components);
    }
}
