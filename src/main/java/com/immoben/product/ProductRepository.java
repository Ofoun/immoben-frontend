package com.immoben.product;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import com.immoben.common.entity.Category;
import com.immoben.common.entity.City;
import com.immoben.common.entity.product.Product;


public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT c FROM Product c WHERE c.id = ?1") 
	public List<Product> findRootProducts(Sort sort);

	 @Query("SELECT c FROM Product c WHERE c.id = ?1") 
	public Page<Product> findRootProducts(Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) ORDER BY p.name ASC")
	public Page<Product> listByCategory (Integer categoryId, String categoryIDMatch, Pageable pageable);


	@Query(value = "SELECT p FROM Product p WHERE p.id = ?1")
	Product getById (Integer id);


	@Query(value = "SELECT p FROM Product p WHERE p.alias LIKE %?1% AND p.id = ?1")
	public Product findByAliasAndId (String alias, Integer id);


	@Query(value = "SELECT p FROM Product p WHERE p.alias LIKE %?1%")
	public Product findByAlias (String alias);


//	@Query(value = "SELECT * FROM products WHERE enabled = true AND MATCH(alias, short_description, full_description, district) AGAINST (?1)", nativeQuery = true)
//	public Page<Product> search (String keyword, Pageable pageable); 

//	@Query(value = "SELECT * FROM products WHERE enabled = true AND MATCH(alias, name, city_name, category_name, short_description, full_description, district) AGAINST (?1)", nativeQuery = true)
//	@Query("SELECT p FROM Product p WHERE (?1 IS NULL OR p.category.name LIKE %?1%) OR (?1 IS NULL OR p.city.name LIKE %?1%) OR (?1 = '' OR p.district LIKE %?1%)")
	@Query("SELECT p FROM Product p WHERE (?1 = '' OR p.category.name = ?1) OR (?1 = '' OR p.city.name = ?1) OR (?1 = '' OR p.district = ?1) OR (?1 = '' OR p.name = ?1) OR (?1 = '' OR p.alias = ?1) OR (?1 = '' OR p.shortDescription = ?1) OR (?1 = '' OR p.fullDescription = ?1) OR (?1 = '' OR p.price <= ?1)")
	public Page<Product> search (String keyword, Category category, City city, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE ((p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) AND " + "(p.name LIKE %?3% " + "OR p.shortDescription LIKE %?3% " + "OR p.fullDescription LIKE %?3% " + "OR p.city.name LIKE %?3% " + "OR p.category.name LIKE %?3%)) AND " + "(p.city.id = ?4 AND " + "(p.name LIKE %?5% " + "OR p.shortDescription LIKE %?5% " + "OR p.fullDescription LIKE %?5% " + "OR p.city.name LIKE %?5% " + "OR p.category.name LIKE %?5%))")
	public Page<Product> searchInCategoryAndCity (String keyword, Integer categoryId, String categoryIdMatch, Integer cityId, String cityIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) AND " + "(p.name LIKE %?3% " + "OR p.shortDescription LIKE %?3% " + "OR p.fullDescription LIKE %?3% " + "OR p.city.name LIKE %?3% " + "OR p.category.name LIKE %?3%)")
	public Page<Product> searchInCategory (String keyword, Integer categoryId, String categoryIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.city.id = ?1 AND (p.name LIKE %?2% " + "OR p.shortDescription LIKE %?2% " + "OR p.fullDescription LIKE %?2% " + "OR p.city.name LIKE %?2% " + "OR p.category.name LIKE %?2%)")
	public Page<Product> searchInCity (String keyword, Integer cityId, String cityIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1% OR p.district LIKE %?1% " + "OR p.shortDescription LIKE %?1% " + "OR p.fullDescription LIKE %?1% " + "OR p.city.name LIKE %?1% " + "OR p.category.name LIKE %?1%")
	public Page<Product> findAll (String keyword, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) AND " + "p.city.id = ?3 ")
	public Page<Product> findAllInCategoryAndCity (Integer categoryId, String categoryIdMatch, Integer cityId, String cityIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAllInCategory (Integer categoryId, String categoryIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.city.id = ?1 ")
	public Page<Product> findAllInCity (Integer cityId, String cityIdMatch, Pageable pageable);


	public Page<Product> findAll (Pageable pageable);


	public List<Product> findByName (String name);


	public Long countById (Integer id);


	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 " + "OR p.category.allParentIDs LIKE %?2% " + "OR p.city.id = ?3 ")
	public Page<Product> findAllInCategoryORCity (Integer categoryId, String categoryIdMatch, Integer cityId, String cityIdMatch, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductsByName (String keyword, Pageable pageable);


	@Query("SELECT p FROM Product p WHERE (?1 IS NULL OR p.category.id = ?1) AND (?2 IS NULL OR p.city.id = ?2) AND (?3 = '' OR p.district = ?3)")
	public Page<Product> search_category_city_district (Integer categoryId, Integer cityId, String district, Pageable pageable);
}