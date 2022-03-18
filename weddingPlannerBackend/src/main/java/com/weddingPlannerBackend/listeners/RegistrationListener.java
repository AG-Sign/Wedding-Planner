package com.weddingPlannerBackend.listeners;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.weddingPlannerBackend.events.OnRegistrationCompleteEvent;
import com.weddingPlannerBackend.model.IUser;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.services.ProviderService;
import com.weddingPlannerBackend.services.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private UserService userService;

	@Autowired
	private ProviderService providerService;

	@Qualifier("messageSource")
	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${baseUrl}")
	private String baseUrl;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		String verifyUrl = "";
		IUser user = event.getUser();
		String token = UUID.randomUUID().toString();
		if (user instanceof User) {
			userService.createVerificationToken((User) user, token);
			verifyUrl = "/api/users/registrationConfirm";
		} else if(user instanceof Provider) {
			providerService.createVerificationToken((Provider) user, token);
			verifyUrl = "/api/providers/registrationConfirm";

		}
		else throw new RuntimeException("invalid type");

		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + verifyUrl + "?token=" + token;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);

		email.setText("Email Confirmation Link\r\n" + baseUrl + confirmationUrl);
		mailSender.send(email);
	}
}
