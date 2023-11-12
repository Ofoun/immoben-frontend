package com.immoben.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.immoben.paging.PagingAndSortingHelper;
import com.immoben.paging.PagingAndSortingParam;

@Controller
public class CityController {
	private String defaultRedirectURL = "redirect:/cities/page/1?sortField=name&sortDir=asc";
	@Autowired private CityService cityService;	
	/* @Autowired private CategoryService categoryService; */
	
	@GetMapping("/cities")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/cities/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listCities", moduleURL = "/cities") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum
			) {
		cityService.listByPage(pageNum, helper);
		return "cities/cities";		
	}
	
//	@GetMapping("/cities/new")
//	public String newCity(Model model) {
//		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//		
//		model.addAttribute("listCategories", listCategories);
//		model.addAttribute("city", new City());
//		model.addAttribute("pageTitle", "Créer une nouvelle marque");
//		
//		return "cities/city_form";
//		
//	}			
	
//	@PostMapping("/cities/save")
//	public String saveCity(City city, @RequestParam("fileImage") MultipartFile multipartFile,
//			RedirectAttributes ra) throws IOException {
//		if (!multipartFile.isEmpty()) {
//			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			city.setLogo(fileName);
//			
//			City savedCity = cityService.save(city);
//			String uploadDir = "city-logos/" + savedCity.getId();
//			
//			AmazonS3Util.removeFolder(uploadDir);
//			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
//			
//			
////			FileUploadUtil.cleanDir(uploadDir);
////			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//			
//		} else {
//			cityService.save(city);
//		}
//		
//		ra.addFlashAttribute("message", "La marque " + city.getName() + " a été enregistrée avec succès !");
//		return defaultRedirectURL;		
//	}
	
//	@GetMapping("/cities/edit/{id}")
//	public String editCity(@PathVariable(name = "id") Integer id, Model model,
//			RedirectAttributes ra) {
//		try {
//			City city = cityService.get(id);
//			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//			
//			model.addAttribute("city", city);
//			model.addAttribute("listCategories", listCategories);
//			model.addAttribute("pageTitle", "Modifier la marque " + city.getName() + " (ID: " + id + ")");
//			
//			return "cities/city_form";			
//		} catch (CityNotFoundException ex) {
//			ra.addFlashAttribute("message", ex.getMessage());
//			return defaultRedirectURL;
//		}
//	}
//	
//	@GetMapping("/cities/delete/{id}")
//	public String deleteCity(@PathVariable(name = "id") Integer id, 
//			Model model,
//			RedirectAttributes redirectAttributes) {
//		try {
//			cityService.delete(id);
//			String cityDir = "city-logos/" + id;
//			AmazonS3Util.removeFolder(cityDir);
//			
//			redirectAttributes.addFlashAttribute("message", 
//					"La marque avec ID " + id + " a été supprimée avec succès !");
//		} catch (CityNotFoundException ex) {
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//		}
//		
//		return defaultRedirectURL;
//	}	
}
