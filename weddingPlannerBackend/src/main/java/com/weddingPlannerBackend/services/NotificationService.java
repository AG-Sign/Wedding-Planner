package com.weddingPlannerBackend.services;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.weddingPlannerBackend.repositories.ReservationRepo;

@Service
public class NotificationService {

	@Autowired
	private ReservationRepo reservationRepo;

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendNotificationEmails() throws MessagingException {
	    List<String> emails = getUsersEmails();
		for (String email : emails)
			sendEmail(email);

	}

	public void sendEmail(String email) throws MessagingException {
		MimeMessage msg = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		helper.setTo(email);

		helper.setSubject("WAITING FOR YOU TOMORROW! CONGRATS");

		helper.setText("Congratulations wish you endless happiness", true);

		helper.addAttachment("congrats.jpg", new ClassPathResource("congrats.jpg"));
		javaMailSender.send(msg);
	}

	private List<String> getUsersEmails() {
		return reservationRepo.getUserEmailsOfTomorrowReservations();
	}

}
