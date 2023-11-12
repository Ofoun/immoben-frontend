package com.immoben.footer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class A_propos_de_nousController {
	
	
	@GetMapping("/aProposDeNous")
	public String showAProposDeNousForm() {
		
		return "footer/a_propos_de_nous";		
	}

}
