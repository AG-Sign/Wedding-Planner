package com.weddingPlannerBackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class LimousineReservationDto {
    long id;
	List<TimeSlotDto> timeSlot;
    LimousineActivityDto limousineActivityDto;
}
