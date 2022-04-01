package com.sesac.foodtruckuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class  FoodtruckUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodtruckUserApplication.class, args);
    }

}
