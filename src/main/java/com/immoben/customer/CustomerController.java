package com.immoben.customer;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.immoben.Utility;
import com.immoben.city.CityRepository;
import com.immoben.common.entity.City;
import com.immoben.common.entity.Country;
import com.immoben.common.entity.Customer;
import com.immoben.common.entity.State;
import com.immoben.security.CustomerUserDetails;
import com.immoben.security.oauth.CustomerOAuth2User;
import com.immoben.setting.CountryRepository;
import com.immoben.setting.EmailSettingBag;
import com.immoben.setting.SettingService;
import com.immoben.setting.StateRepository;

@Controller
public class CustomerController {
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	@Autowired private CountryRepository countryRepo;
	@Autowired private StateRepository stateRepo;
	@Autowired private CityRepository cityRepo;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Country> listCountries = customerService.listAllCountries();
		List<State> listStates = customerService.listAllStates();
		List<City> listCities = customerService.listAllCities();

		Customer customer = new Customer();
		
		Integer id = 26;		
		Country country = countryRepo.findById(id).get();		
		customer.setCountry(country);
		
		Integer id1 = 8;
		State state = stateRepo.findById(id1).get();
		customer.setState(state);
		
		Integer id2 = 1;
		City city = cityRepo.findById(id2).get();
		customer.setCity(city);
		
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("listStates", listStates);
		model.addAttribute("listCities", listCities);
		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", customer);
		
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model,
			HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		
		model.addAttribute("pageTitle", "Inscription réussie !");
		
		return "register/register_success";
	}

	private void sendVerificationEmail(HttpServletRequest request, Customer customer) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName());
		
		String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL: " + verifyURL);
	}	
	
	@GetMapping("/verify")
	public String verifyAccount(String code, Model model) {
		boolean verified = customerService.verify(code);
		
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
	
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		
		Customer customer = customerService.getCustomerByEmail(email);
		
		List<Country> listCountries = customerService.listAllCountries();
		List<State> listStates = customerService.listAllStates();
		List<City> listCities = customerService.listAllCities();
		
		model.addAttribute("customer", customer);
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("listStates", listStates);
		model.addAttribute("listCities", listCities);
		
		return "customer/account_form";
	}
	
	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra,
			HttpServletRequest request) {
		customerService.update(customer);
		ra.addFlashAttribute("message", "Les détails de votre compte ont été mis à jour avec succès.");
		
		updateNameForAuthenticatedCustomer(customer, request);
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/account_details";
		
		if ("address_book".equals(redirectOption)) {
			redirectURL = "redirect:/address_book";
		} else if ("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		} else if ("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/address_book?redirect=checkout";
		}
		
		return redirectURL;
	}

	private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		
		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
			Customer authenticatedCustomer = userDetails.getCustomer();
			authenticatedCustomer.setFirstName(customer.getFirstName());
			authenticatedCustomer.setLastName(customer.getLastName());
			
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			String fullName = customer.getFirstName() + " " + customer.getLastName();
			oauth2User.setFullName(fullName);
		}		
	}
	
	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		if (principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		} else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}
		
		return userDetails;
	}
}
