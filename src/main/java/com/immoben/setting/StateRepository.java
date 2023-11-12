package com.immoben.setting;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.immoben.common.entity.Country;
import com.immoben.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
	
	public Optional<State> findById(Integer id);
	
	public List<State> findAllByOrderByNameAsc();

}
