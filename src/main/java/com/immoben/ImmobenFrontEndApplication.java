package com.immoben;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.immoben.common.entity", "com.immoben.customer"})
public class ImmobenFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImmobenFrontEndApplication.class, args);
	}

}
