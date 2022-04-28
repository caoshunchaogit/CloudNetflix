package com.csc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudEurekaProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudEurekaProviderApplication.class, args);
    }

}
