package com.weddingPlannerBackend.repositories;

import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepo extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByActivityTypeAndActivityId(String activityType, Long activityId);
}
