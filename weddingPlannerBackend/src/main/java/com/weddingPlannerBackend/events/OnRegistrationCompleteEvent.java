package com.weddingPlannerBackend.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.weddingPlannerBackend.model.IUser;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appUrl;
    private Locale locale;
    private IUser user;

    public OnRegistrationCompleteEvent(IUser user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }


}
