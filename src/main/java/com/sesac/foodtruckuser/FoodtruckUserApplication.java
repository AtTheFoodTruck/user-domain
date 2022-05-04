package com.sesac.foodtruckuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@EnableJpaAuditing
@EnableEurekaClient
@SpringBootApplication
public class FoodtruckUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodtruckUserApplication.class, args);
    }

}
