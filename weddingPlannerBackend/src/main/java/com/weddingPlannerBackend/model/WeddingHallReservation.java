package com.weddingPlannerBackend.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class WeddingHallReservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "wedding_hall_activity_id", nullable = true)
	private WeddingHallActivity weddingHallActivity;

	@ManyToOne
	@JoinColumn(name = "reservation_id", nullable = true)
	private Reservation reservation;
	
	@ManyToOne
	@JoinColumn(name = "time_slot_id", nullable = true)
	private TimeSlot timeSlot;

}
