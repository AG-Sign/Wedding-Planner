package com.weddingPlannerBackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class StylistReservationDto {
	long id;
	StylistActivityDto stylistActivityDto;
	List<TimeSlotDto> timeSlot;
}
