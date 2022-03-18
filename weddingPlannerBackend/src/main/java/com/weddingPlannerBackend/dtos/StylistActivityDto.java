package com.weddingPlannerBackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class StylistActivityDto {
    private Long id;
    private String description;
    private double price;
    private String img;
    private ProviderDto provider;
    private List<TimeSlotDto> timeslots;
}
