package com.immoben.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.immoben.common.entity.Customer;
import com.immoben.customer.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService {
	
	@Autowired private CustomerRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = repo.findByEmail(email);
		if (customer == null) 
			throw new UsernameNotFoundException("Aucun client avec l'e-mail: "  + email + " n'a été trouvé !");
		
		return new CustomerUserDetails(customer);
	}

}
