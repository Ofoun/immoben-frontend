package com.immoben.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class CustomerRestController {

	@Autowired private CustomerService service;
	
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(String email) {
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
