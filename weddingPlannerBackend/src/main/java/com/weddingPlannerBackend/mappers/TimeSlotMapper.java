package com.weddingPlannerBackend.mappers;

import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.model.TimeSlot;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class TimeSlotMapper {

    public TimeSlotDto toDto(TimeSlot timeSlot) {
        if (timeSlot == null)
            return null;

        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setId(timeSlot.getId());
        timeSlotDto.setActivityId(timeSlot.getActivityId());
        timeSlotDto.setActivityType(timeSlot.getActivityType());
        timeSlotDto.setStartTime(timeSlot.getStartTime());
        timeSlotDto.setEndTime(timeSlot.getEndTime());

        return timeSlotDto;
    }

    public TimeSlot fromDto(TimeSlotDto timeSlotDto) {
        TimeSlot timeSlot = new TimeSlot();

        timeSlot.setId(timeSlotDto.getId());
        timeSlot.setActivityId(timeSlotDto.getActivityId());
        timeSlot.setActivityType(timeSlotDto.getActivityType());
        timeSlot.setStartTime(timeSlotDto.getStartTime());
        timeSlot.setEndTime(timeSlotDto.getEndTime());

        return timeSlot;
    }
}
