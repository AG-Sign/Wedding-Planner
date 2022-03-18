package com.weddingPlannerBackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class WeddingHallReservationDto {
	long id;
	WeddingHallActivityDto weddingHallActivityDto;
	List<TimeSlotDto> timeSlot;

}
