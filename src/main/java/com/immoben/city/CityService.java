package com.immoben.city;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.immoben.paging.PagingAndSortingHelper;
import com.immoben.common.entity.City;

@Service
public class CityService {
	public static final int CITIES_PER_PAGE = 20;
	
	@Autowired
	private CityRepository repo;
	
	public List<City> listAll() {
		return (List<City>) repo.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CITIES_PER_PAGE, repo);
	}
	
	public City save(City city) {
		return repo.save(city);
	}
	
	public City get(Integer id) throws CityNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CityNotFoundException("Aucune marque avec ID: " + id + " n'est trouvable.");
		}
	}
	
	public void delete(Integer id) throws CityNotFoundException {
		Long countById = repo.countById(id);
		
		if (countById == null || countById == 0) {
			throw new CityNotFoundException("Aucune marque avec ID: " + id + " n'est trouvable.");			
		}
		
		repo.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		City cityByName = repo.findByName(name);
		
		if (isCreatingNew) {
			if (cityByName != null) return "Duplicate";
		} else {
			if (cityByName != null && cityByName.getId() != id) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
	


	
	public List<City> listCitiesUsedInForm() {
		List<City> citiesUsedInForm = new ArrayList<>();
		
		Iterable<City> citiesInDB = repo.findRootCities(Sort.by("name").ascending());
		
		for (City city : citiesInDB) {
			citiesUsedInForm.add(City.copyIdAndName(city));
			
//			Set<City> children = sortSubCities(city.getChildren());
			
//			for (City subCity : children) {
//				String name = "--" + subCity.getName();
//				citiesUsedInForm.add(City.copyIdAndName(subCity.getId(), name));
//				
//				listSubCitiesUsedInForm(citiesUsedInForm, subCity, 1);
//			}

		}		
		
		return citiesUsedInForm;
	}
	
//	private void listSubCitiesUsedInForm(List<City> citiesUsedInForm, 
//			 int subLevel) {
//		int newSubLevel = subLevel + 1;
//		Set<City> children = sortSubCities(parent.getChildren());
//		
//		for (City subCity : children) {
//			String name = "";
//			for (int i = 0; i < newSubLevel; i++) {				
//				name += "--";
//			}
//			name += subCity.getName();
//			
//			citiesUsedInForm.add(City.copyIdAndName(subCity.getId(), name));
//			
//			listSubCitiesUsedInForm(citiesUsedInForm, subCity, newSubLevel);
//		}		
//	}	
}
