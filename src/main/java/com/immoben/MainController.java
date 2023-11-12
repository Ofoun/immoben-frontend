package com.immoben;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.immoben.category.CategoryService;
import com.immoben.city.CityService;
import com.immoben.common.entity.Category;
import com.immoben.common.entity.City;
import com.immoben.common.entity.product.Product;
import com.immoben.paging.PagingAndSortingHelper;
import com.immoben.paging.PagingAndSortingParam;
import com.immoben.product.ProductRepository;
import com.immoben.product.ProductService;

@Controller
public class MainController {
//	private String defaultRedirectURL = "redirect:/product/page/1?sortField=id&sortDir=desc&categoryId=0&cityId=0&keyword= ";
	
	@Autowired private CategoryService categoryService;	
	@Autowired private ProductService productService;
	@Autowired private CityService cityService;	
	@Autowired private ProductRepository repo;
	
	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}
	
	@GetMapping("/")
	public String listFirstPage(Model model) {
		return listByPage(null, 1, model, "id", "desc", null, 0, 0);
	}

		
	@GetMapping("/product/page/{pageNum}")
	public String listByPage (
	  	    @PagingAndSortingParam(listName = "listProducts", moduleURL = "/product") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, 
			@Param("sortDir") String sortDir,
			@Param("keyword") String keyword,
			@Param("categoryId") Integer categoryId,
			@Param("cityId") Integer cityId
	) {
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId, cityId);
		List<Product> listProducts = page.getContent();
		
		// List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		List<Category> listCategories = categoryService.listNoChildrenCategories();
		
		List<City> listCities = cityService.listAll();
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		if (categoryId != null) model.addAttribute("categoryId", categoryId); 
		if (cityId != null) model.addAttribute("cityId", cityId); 
		
		model.addAttribute("helper", helper);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);		
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("listCities", listCities);	
		model.addAttribute("moduleURL", "/product");	
		
		return "index";		
	}


	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
	}	
}
