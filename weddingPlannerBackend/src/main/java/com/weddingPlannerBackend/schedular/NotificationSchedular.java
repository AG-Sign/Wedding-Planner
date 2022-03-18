package com.weddingPlannerBackend.schedular;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.weddingPlannerBackend.services.NotificationService;

@Component
public class NotificationSchedular {

	@Autowired
	private NotificationService notificationService;

	@Scheduled(cron = "0 0 18 * * *")
	public void sendNotificationJob() {
		try {
			System.out.println("SENDING MAIL ..");
			notificationService.sendNotificationEmails();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
