package com.weddingPlannerBackend.dtos;

import java.util.List;

import com.weddingPlannerBackend.model.WeddingHallReservation;

import lombok.Data;

@Data
public class MainReservationDto {

	List<LimousineReservationDto> limousineReservations;
	List<StylistReservationDto> stylistReservations;
	List<WeddingHallReservation> weddingHallReservations;
}
