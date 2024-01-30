package com.einsbym.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EinsbymGateway {
	public static void main(String[] args) {
		SpringApplication.run(EinsbymGateway.class, args);
	}
}
