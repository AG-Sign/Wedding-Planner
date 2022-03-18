package com.weddingPlannerBackend.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class User implements IUser{

	private String address;

	@OneToMany(mappedBy= "user")
    Set<Reservation> userReservations;

	private String name;

	@Id
	@Column(unique = true)
	private String email;

	private String password;

	private boolean isVerified;
}
