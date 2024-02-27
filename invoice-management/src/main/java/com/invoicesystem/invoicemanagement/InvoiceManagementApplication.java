package com.invoicesystem.invoicemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InvoiceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceManagementApplication.class, args);
	}

}
