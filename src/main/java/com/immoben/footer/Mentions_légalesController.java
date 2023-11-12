package com.immoben.footer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Mentions_légalesController {
	
	@GetMapping("/mentionsLégales")
	public String showMentionsLégales() {
		
		return "footer/mentions_légales";		
	}

}
