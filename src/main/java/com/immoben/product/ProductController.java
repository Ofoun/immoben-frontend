package com.immoben.product;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.immoben.category.CategoryService;
import com.immoben.common.entity.Category;
import com.immoben.common.entity.City;
import com.immoben.common.entity.product.Product;
import com.immoben.common.exception.CategoryNotFoundException;
import com.immoben.common.exception.ProductNotFoundException;


@Controller
public class ProductController {
	private String defaultRedirectURL = "redirect:/product/page/1?sortField=id&sortDir=desc&categoryId=0&cityId=0&keyword=null ";

	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/c/{category_alias}")
	public String viewCategoryFirstPage (@PathVariable("category_alias") String alias, Model model) {
		return this.viewCategoryByPage(alias, 1, model);
	}


	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewCategoryByPage (@PathVariable("category_alias") String alias, @PathVariable("pageNum") int pageNum, Model model) {
		try {
			Category category = categoryService.getCategory(alias);
			List<Category> listCategoryParents = categoryService.getCategoryParents(category);

			Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
			List<Product> listProducts = pageProducts.getContent();

			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}

			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("pageTitle", category.getName());
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("listProducts", listProducts);
			model.addAttribute("category", category);

			return "product/products_by_category";
		} catch (CategoryNotFoundException ex) {
			return "error/404";
		}
	}


	@GetMapping("/p/{product_id}")
	public String viewProductDetail (@PathVariable("product_id") Integer id, Model model, HttpServletRequest request) {
		try {
			Product product = productService.getProduct(id);
			List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());

			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getShortName());

			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			return "error/404";
		}
	}


	@GetMapping("/search")
	public String searchFirstPage (String keyword, Category category, City city, Model model) {
		
		return this.searchByPage(keyword, 1, category, city, model);
	}


	@GetMapping("/search/page/{pageNum}")
	public String searchByPage (String keyword, @PathVariable("pageNum") int pageNum, Category category, City city, Model model) {
		Page<Product> pageProducts = productService.search(keyword, category, city, pageNum);
		List<Product> listResult = pageProducts.getContent();

		long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("pageTitle", keyword + " - Search Result");

		model.addAttribute("keyword", keyword);
		model.addAttribute("listResult", listResult);

		return "product/search_result";
	}

	/*------------------------------------------------------------*/


	@GetMapping("/search_category_city_district")
	public String searchFirstHomePage (Category categoryIdMatch, City cityIdMatch, String keyword, Model model) {
		return this.searchByHomePage(categoryIdMatch, cityIdMatch, keyword, 1, model);
	}


	@GetMapping("/search_category_city_district/page/{pageNum}")
	public String searchByHomePage (Category category, City city, String district, @PathVariable("pageNum") int pageNumber, Model model) {
		final Page<Product> pageProducts = this.productService.search_category_city_district(category.getId(), city.getId(), district, pageNumber);
		final List<Product> products = pageProducts.getContent();

		final long startCount = (pageNumber - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements())
			endCount = pageProducts.getTotalElements();

		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("pageTitle", district + " - Search Result");
		model.addAttribute("categoryIdMatch", category);
		model.addAttribute("cityIdMatch", city);
		model.addAttribute("keyword", district);
		model.addAttribute("listResult", products);

		return "product/search_home_page_result";
	}

//	@GetMapping("/product/detail/{alias}")
//	public String viewProductDetails( @PathVariable("alias") String alias, Model model,
//			RedirectAttributes ra) {
//		try {
//			Product product = productService.getProduct(alias);			
//			model.addAttribute("product", product);		
//			
//			return "product/product_detail_modal";
//			
//		} catch (ProductNotFoundException e) {
//			ra.addFlashAttribute("message", e.getMessage());
//			
//			return defaultRedirectURL;
//		}
//	  }	


	@GetMapping("/product/detail/{id}")
	public String viewProductDetails (@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			model.addAttribute("product", product);

			return "product/product_detail_modal";

		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());

			return defaultRedirectURL;
		}
	}
}
