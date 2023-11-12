package com.immoben.setting;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.immoben.common.entity.Country;
import com.immoben.common.entity.State;


@RestController
@CrossOrigin(origins = "*")
public class StateRestController {

	@Autowired
	private StateRepository repo;

	@GetMapping("/settings/list_states_by_country/{id}")
	public List<State> listByCountry (@PathVariable("id") Integer countryId) {
		List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));
		List<State> result = new ArrayList<>();

		for (State state : listStates) {
			result.add(new State(state.getId(), state.getName(), null));
		}

		return result;
	}

}
