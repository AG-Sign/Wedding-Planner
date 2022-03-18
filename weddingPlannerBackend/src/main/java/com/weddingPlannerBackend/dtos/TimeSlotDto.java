package com.weddingPlannerBackend.dtos;

import lombok.Data;

@Data
public class TimeSlotDto {
    private Long id;
    private Long activityId;
    private String activityType;
    private String startTime;
    private String endTime;
}