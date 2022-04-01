package com.sesac.foodtruckuser.application.doc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
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
    private static final String API_TITLE = "Food Truck Around Me";
    private static final String API_VERSION = "v1";
    private static final String API_DESCRIPTION = "Sample API Docs use Swagger";

    private static final Set<String> PRODUCES = new HashSet<>(Collections.singletonList(
            "application/json"
    ));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .produces(PRODUCES)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .contact(new Contact("Sesac FinalProject","",""))
                .build();
    }
}
