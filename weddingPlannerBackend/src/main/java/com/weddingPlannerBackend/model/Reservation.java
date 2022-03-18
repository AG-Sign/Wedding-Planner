package com.weddingPlannerBackend.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "user_email")
	private User user;
	public Reservation(Long id, User user, java.util.Date date, boolean confirmed, String paymentMethod) {
		super();
		this.id = id;
		this.user = user;
		Date = date;
		this.confirmed = confirmed;
		this.paymentMethod = paymentMethod;
	}
	public Reservation() {
		// TODO Auto-generated constructor stub
	}
	@Basic
	@Temporal(TemporalType.DATE)
	private Date Date;
	private boolean confirmed;
	private String paymentMethod;

	@OneToMany(mappedBy= "reservation", cascade = CascadeType.REMOVE)
	Set<WeddingHallReservation> weddingHalls;

	@OneToMany(mappedBy= "reservation", cascade = CascadeType.REMOVE)
	Set<LimousineReservation> limousineActivities;

	@OneToMany(mappedBy= "reservation", cascade = CascadeType.REMOVE)
	Set<StylistReservation> stylists;
}
