package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.TimeSlot;

public interface ITimeSlotService {
    TimeSlot CreateTimeSlot(TimeSlotDto timeSlotDto, Provider provider);
}
