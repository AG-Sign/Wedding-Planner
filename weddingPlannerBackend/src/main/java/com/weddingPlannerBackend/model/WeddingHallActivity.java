package com.weddingPlannerBackend.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class WeddingHallActivity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer numberOfGuests;
	private boolean hasRoom;
	private double price;
	private String img;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider", nullable = true)
	private Provider provider;

}
