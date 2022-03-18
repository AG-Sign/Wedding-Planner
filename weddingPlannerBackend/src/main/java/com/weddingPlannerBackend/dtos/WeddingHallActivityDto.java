package com.weddingPlannerBackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class WeddingHallActivityDto {
    private Long id;
    private Integer numberOfGuests;
    private boolean hasRoom;
    private double price;
    private String img;
    private ProviderDto provider;
    private List<TimeSlotDto> timeslots;
    
}
