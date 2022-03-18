package com.weddingPlannerBackend.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class AuthenticationToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(targetEntity = Provider.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private Date expiryDate;

    public AuthenticationToken(String token) {
        this.token = token;
        expiryDate = calculateExpiryDate();
    }

    public AuthenticationToken() {
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
}
