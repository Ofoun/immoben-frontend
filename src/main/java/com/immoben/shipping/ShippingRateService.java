package com.immoben.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.immoben.common.entity.Address;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.ShippingRate;
import com.immoben.common.entity.State;

@Service
public class ShippingRateService {

	@Autowired private ShippingRateRepository repo;
	
	public ShippingRate getShippingRateForCustomer(Customer customer) {
		State state = customer.getState();
		if (state == null || state.isEmpty()) {
			state = customer.getState();
		}
		
		return repo.findByCountryAndState(customer.getCountry(), state);
	}
	
	public ShippingRate getShippingRateForAddress(Address address) {
		State state = address.getState();
		if (state == null || state.isEmpty()) {
			state = address.getState();
		}
		
		return repo.findByCountryAndState(address.getCountry(), state);
	}	
}
