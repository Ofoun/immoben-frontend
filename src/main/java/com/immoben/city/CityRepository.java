package com.immoben.city;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import com.immoben.common.entity.City;
import com.immoben.paging.SearchRepository;

public interface CityRepository extends SearchRepository<City, Integer> {
	@Query("SELECT c FROM City c WHERE c.id = ?1") 
	public List<City> findRootCities(Sort sort);

	 @Query("SELECT c FROM City c WHERE c.id = ?1") 
	public Page<City> findRootCities(Pageable pageable);

	public Long countById(Integer id);
	
	public City findByName(String name);
	
	public List<City> findAllByOrderByNameAsc();
	
	@Query("SELECT c FROM City c WHERE c.name LIKE %?1%")
	public Page<City> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT NEW City(c.id, c.name) FROM City c ORDER BY c.name ASC")
	public List<City> findAll();
	
	@Query("SELECT c FROM City c WHERE c.name LIKE %?1%")
	public Page<City> search(String keyword, Pageable pageable);
		
	public Optional<City> findById(Integer id);
}
