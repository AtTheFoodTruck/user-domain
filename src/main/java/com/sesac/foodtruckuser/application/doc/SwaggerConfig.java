package com.sesac.foodtruckuser.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@Profile({"local", "dev"})
public class SwaggerConfig {
    private static final Set<String> PRODUCES = new HashSet<>(Collections.singletonList(
            "application/json"
    ));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .produces(PRODUCES)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("24/7 CloudLibrary Service API",
                "Sample API Docs use Swagger",
                "V1",
                "",
                new Contact("","",""),
                "FinalProject",
                "",
                Collections.emptyList());
    }
}
