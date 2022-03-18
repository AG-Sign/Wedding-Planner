package com.weddingPlannerBackend.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class StylistActivity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private double price;
	private String img;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "provider", nullable = true)
	private Provider provider;
}
