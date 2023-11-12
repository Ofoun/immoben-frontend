package com.immoben.contact;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ContactController {
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	@GetMapping("/contact")
	public String showContactForm() {
		
		return "contact_form";
	}
	
	@PostMapping("/contact")
	public String submitContact(HttpServletRequest request,
			@RequestParam("attachment") MultipartFile multipartFile
			) throws MessagingException, UnsupportedEncodingException {
		String fullname = request.getParameter("fullname");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		
		
		String mailSubject = fullname + " vous a envoyé un message";
		String mailContent = "<p><b>Nom de l'expéditeur:</b> " + fullname + "</p>";
		mailContent += "<p><b>E-mail de l'expéditeur:</b> " + email +  "</p>";
		mailContent += "<p><b>Téléphone de l'expéditeur:</b> " + phone +  "</p>";
		mailContent += "<p><b>Objet:</b> " + subject +  "</p><br/>";
		mailContent += "<p><b>Contenu:</b><br/> " + content +  "</p><br/><br/><br/><br/>";
		mailContent += "<hr><img src='cid:logoImage' />";
		
		helper.setFrom("immobenbenin@googlemail.com", "IMMOBEN CONTACT");
		helper.setTo("info@immoben.fr");
		helper.setSubject(mailSubject);
		helper.setText(mailContent, true);
		
		ClassPathResource resource = new ClassPathResource("/static/images/Immoben.png");
		helper.addInline("logoImage", resource);
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			InputStreamSource source = new InputStreamSource() {
				
				@Override
				public InputStream getInputStream() throws IOException {
					
					return multipartFile.getInputStream();
				}
			};
			
			helper.addAttachment(fileName, source);
		}
		
		mailSender.send(message);
		
		return "contact_message";
	}

}
