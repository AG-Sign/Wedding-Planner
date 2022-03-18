package com.weddingPlannerBackend.model;

import java.util.Set;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.springframework.data.repository.cdi.Eager;

@Data
@Entity
public class Provider implements IUser {

	private String name;
	private String address;
	private boolean isVerified;

	@Id
	@Column(unique = true)
	private String email;
	private String password;

	@OneToMany(mappedBy= "provider")
	Set<WeddingHallActivity> weddingHalls;

	@OneToMany(mappedBy= "provider")
	Set<LimousineActivity> limousineActivities;

	@OneToMany(mappedBy= "provider")
	Set<StylistActivity> stylists;
}
