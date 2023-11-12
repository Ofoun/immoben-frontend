package com.immoben.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.immoben.Utility;
import com.immoben.common.entity.Customer;
import com.immoben.common.exception.CustomerNotFoundException;
import com.immoben.common.exception.ProductNotFoundException;
import com.immoben.customer.CustomerService;

@RestController
@CrossOrigin(origins = "*")
public class ShoppingCartRestController {

	@Autowired
	private ShoppingCartService cartService;
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
			
			return updatedQuantity + " unité(s) de ce produit a(ont) été ajouté(s) à votre panier.";
		} catch (CustomerNotFoundException ex) {
			return "Vous devez vous connecter pour ajouter ce produit au panier.";
		} catch (ShoppingCartException ex) {
			return ex.getMessage();
		}
		
	}
	
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) 
				throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("Aucun client authentifié");
		}
		
		
		return customerService.getCustomerByEmail(email);
	}
	
	
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
	
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subtotal = cartService.updateQuantity(productId, quantity, customer);
			
			return String.valueOf(subtotal);
		} catch (CustomerNotFoundException ex) {
			return "Vous devez vous connecter pour modifier la quantité du produit dans le panier.";
		
		}
	}

	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,
			HttpServletRequest request) throws ProductNotFoundException {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer);
			
			return "Ce produit a été retiré de votre panier.";
			
		} catch (CustomerNotFoundException e) {
			return "Vous devez d'abord vous connecter pour supprimer le produit.";
		}
	}
	
	
}
