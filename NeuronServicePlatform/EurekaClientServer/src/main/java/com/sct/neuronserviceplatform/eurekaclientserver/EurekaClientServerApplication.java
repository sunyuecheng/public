package com.sct.neuronserviceplatform.eurekaclientserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientServerApplication.class, args);
	}
}
