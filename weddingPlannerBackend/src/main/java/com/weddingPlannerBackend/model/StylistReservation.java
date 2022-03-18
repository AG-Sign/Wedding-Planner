package com.weddingPlannerBackend.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class StylistReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "stylist_activity_id", nullable = true)
	private StylistActivity stylistActivity;

	@ManyToOne
	@JoinColumn(name = "reservation_id", nullable = true)
	private Reservation reservation;
	
	@ManyToOne
	@JoinColumn(name = "time_slot_id", nullable = true)
	private TimeSlot timeSlot;
}
