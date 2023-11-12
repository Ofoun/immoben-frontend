package com.immoben.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.immoben.common.entity.Customer;
import com.immoben.common.entity.Role;

public class CustomerUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private Customer customer;
	
	public CustomerUserDetails(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = customer.getRoles();
		
		List<SimpleGrantedAuthority> authories = new ArrayList<>();
		
		for (Role role : roles) {
			authories.add(new SimpleGrantedAuthority(role.getDescription()));
		}
		
		return authories;
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return customer.isEnabled();
	}

	public String getFullName() {
		return customer.getFirstName() + " " + customer.getLastName();
	}
	
	public Customer getCustomer() {
		return this.customer;
	}
}
