package com.weddingPlannerBackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class LimousineActivityDto {
    private Long id;
    private String carType;
    private double price;
    private String img;
    private ProviderDto provider;
    private List<TimeSlotDto> timeslots;
}
