package com.weddingPlannerBackend.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class TimeSlot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long activityId;
	private String activityType;
	private String startTime;
	private String endTime;

}
