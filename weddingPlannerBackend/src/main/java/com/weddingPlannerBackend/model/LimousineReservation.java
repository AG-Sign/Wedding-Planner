package com.weddingPlannerBackend.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class LimousineReservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name = "limousine_activity_id", nullable = false)
	private LimousineActivity limousineActivity;

	@ManyToOne
	@JoinColumn(name = "reservation_id", nullable = true)
	private Reservation reservation;
	
	@ManyToOne
	@JoinColumn(name = "time_slot_id", nullable = true)
	private TimeSlot timeSlot;
	
}
