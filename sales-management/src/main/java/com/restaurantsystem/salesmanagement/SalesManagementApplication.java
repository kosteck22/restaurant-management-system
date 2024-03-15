package com.restaurantsystem.salesmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@SpringBootApplication
@EnableFeignClients
public class SalesManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesManagementApplication.class, args);
	}

}
