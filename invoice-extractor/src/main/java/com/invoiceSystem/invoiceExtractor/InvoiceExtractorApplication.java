package com.invoiceSystem.invoiceExtractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InvoiceExtractorApplication {
	static {
		System.setProperty("jna.library.path", "/opt/homebrew/Cellar/tesseract/5.3.4/lib");
	}

	public static void main(String[] args) {
		SpringApplication.run(InvoiceExtractorApplication.class, args);
	}

}
