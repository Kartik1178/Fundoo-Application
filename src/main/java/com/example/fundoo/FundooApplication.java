package com.example.fundoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FundooApplication {
    public static void main(String[] args) {
        SpringApplication.run(FundooApplication.class, args);
    }
}
