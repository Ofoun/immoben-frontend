package com.immoben.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.immoben.Utility;
import com.immoben.common.entity.Customer;
import com.immoben.common.exception.CustomerNotFoundException;
import com.immoben.setting.EmailSettingBag;
import com.immoben.setting.SettingService;

@Controller
public class ForgotPasswordController {
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			sendEmail(link, email);
			
			model.addAttribute("message", "Nous avons envoyé un lien de réinitialisation du mot de passe à votre adresse e-mail."
					+ " Vérifiez s'il vous plaît.");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Impossible d'envoyer un e-mail");
		}
		
		return "customer/forgot_password_form";
	}
	
	private void sendEmail(String link, String email) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "Voici le lien pour réinitialiser votre mot de passe";
		
		String content = "<p>Allo,</p>"
				+ "<p>Vous avez demand&eacute; la r&eacute;initialisation de votre mot de passe.</p>"
				+ "Cliquez sur le lien ci-dessous pour changer votre mot de passe:</p>"
				+ "<p><a href=\"" + link + "\">Changer mon mot de passe</a></p>"
				+ "<br>"
				+ "<p>Ignorez cet e-mail si vous vous souvenez de votre mot de passe, "
				+ "ou si vous n&#039;avez pas fait la demande .</p>"; 
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);		
		
		helper.setText(content, true);
		mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if (customer != null) {
			model.addAttribute("token", token);
		} else {
			model.addAttribute("pageTitle", "Jeton invalide");
			model.addAttribute("message", "Jeton invalide");
			return "message";
		}
		
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			
			model.addAttribute("pageTitle", "Réinitialisez votre mot de passe");
			model.addAttribute("title", "Réinitialisation du mot de passe");
			model.addAttribute("message", "Vous avez changé votre mot de passe avec succès. ");
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Jeton invalide");
			model.addAttribute("message", e.getMessage());
		}	
		

		return "message";		
	}
}
