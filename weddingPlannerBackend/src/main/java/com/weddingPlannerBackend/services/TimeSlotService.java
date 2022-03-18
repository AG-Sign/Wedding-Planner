package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.mappers.TimeSlotMapper;
import com.weddingPlannerBackend.model.*;
import com.weddingPlannerBackend.repositories.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
public class TimeSlotService implements ITimeSlotService {

    @Autowired
    private WeddingHallActivityRepo weddingHallActivityRepo;
    @Autowired
    private LimousineActivityRepo limousineActivityRepo;
    @Autowired
    private StylistActivityRepo stylistActivityRepo;
    @Autowired
    private WeddingHallReservationRepo weddingHallReservationRepo;
    @Autowired
    private LimousineReservationRepo limousineReservationRepo;
    @Autowired
    private StylistReservationRepo stylistReservationRepo;
    @Autowired
    private TimeSlotRepo timeSlotRepo;
    @Autowired
    private TimeSlotMapper timeSlotMapper;

    public TimeSlot CreateTimeSlot(TimeSlotDto timeSlotDto, Provider provider) {
        if (!isValidTime(timeSlotDto.getStartTime()) || !isValidTime(timeSlotDto.getEndTime()))
            return null;

        TimeSlot timeSlot = timeSlotMapper.fromDto(timeSlotDto);
        if (!validateTimeSlot(timeSlot, provider))
            return null;
        return timeSlotRepo.save(timeSlot);
    }

    public Boolean deleteTimeSlot(TimeSlotDto timeSlotDto, Provider provider) {
        if (!isValidTime(timeSlotDto.getStartTime()) || !isValidTime(timeSlotDto.getEndTime())) return null;
        TimeSlot timeSlot = timeSlotMapper.fromDto(timeSlotDto);
        return delete(timeSlot, provider);
    }

    @SneakyThrows
    private boolean validateTimeSlot(TimeSlot timeSlot, Provider provider) {
        if (timeSlot.getStartTime() == null || timeSlot.getEndTime() == null
                || timeSlot.getActivityType() == null)
            return false;

        DateFormat sdf = new SimpleDateFormat("HH:mm");
        if (sdf.parse(timeSlot.getStartTime()).after(sdf.parse(timeSlot.getEndTime())))
            return false;

        switch (timeSlot.getActivityType()) {
            case "WeddingHall":
                return validateWeddingHallTimeSlot(timeSlot, provider);
            case "Limousine":
                return validateLimousineTimeSlot(timeSlot, provider);
            case "Stylist":
                return validateStylistTimeSlot(timeSlot, provider);
        }
        return false;
    }


    private boolean validateWeddingHallTimeSlot(TimeSlot timeSlot, Provider provider) {
        WeddingHallActivity activity = weddingHallActivityRepo.findById((long)timeSlot.getActivityId());
        if (activity == null || !activity.getProvider().getEmail().equals(provider.getEmail())) return false;

        return validateTimeSlotOverlap(timeSlot);
    }

    private boolean validateLimousineTimeSlot(TimeSlot timeSlot, Provider provider) {
        LimousineActivity activity = limousineActivityRepo.findById((long)timeSlot.getActivityId());
        if (activity == null || !activity.getProvider().getEmail().equals(provider.getEmail())) return false;

        return validateTimeSlotOverlap(timeSlot);
    }

    private boolean validateStylistTimeSlot(TimeSlot timeSlot, Provider provider) {
        StylistActivity activity = stylistActivityRepo.findById((long)timeSlot.getActivityId());
        if (activity == null || !activity.getProvider().getEmail().equals(provider.getEmail())) return false;

        return validateTimeSlotOverlap(timeSlot);
    }

    @SneakyThrows
    private boolean validateTimeSlotOverlap(TimeSlot timeSlot) {
        List<TimeSlot> timeSlots = timeSlotRepo.findByActivityTypeAndActivityId(timeSlot.getActivityType(),
                                                                                timeSlot.getActivityId());
        DateFormat sdf = new SimpleDateFormat("HH:mm");
        for (TimeSlot slot: timeSlots) {
            if (sdf.parse(slot.getStartTime()).getTime() > sdf.parse(timeSlot.getEndTime()).getTime()
                || sdf.parse(slot.getEndTime()).getTime() < sdf.parse(timeSlot.getStartTime()).getTime())
                continue;
            return false;
        }
        return true;
    }

    public boolean isValidTime(String time) {
        DateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(time);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private Boolean delete(TimeSlot timeSlot, Provider provider) {
        if (!isValidProvider(timeSlot, provider)) return null;
        if (hasUsers(timeSlot)) return false;
        timeSlotRepo.delete(timeSlot);
        return true;
    }

    private boolean isValidProvider(TimeSlot timeSlot, Provider provider) {
        switch (timeSlot.getActivityType()) {
            case "WeddingHall":
                WeddingHallActivity weddingHallActivity = weddingHallActivityRepo.findById((long)timeSlot.getActivityId());
                return weddingHallActivity != null && weddingHallActivity.getProvider().getEmail().equals(provider.getEmail());
            case "Limousine":
                LimousineActivity limousineActivity = limousineActivityRepo.findById((long)timeSlot.getActivityId());
                return limousineActivity != null && limousineActivity.getProvider().getEmail().equals(provider.getEmail());
            case "Stylist":
                StylistActivity stylistActivity = stylistActivityRepo.findById((long)timeSlot.getActivityId());
                return stylistActivity != null && stylistActivity.getProvider().getEmail().equals(provider.getEmail());
            default:
                return false;
        }
    }

    private boolean hasUsers(TimeSlot timeSlot) {
        switch (timeSlot.getActivityType()) {
            case "WeddingHall":
                return weddingHallReservationRepo.getCountByTimeSlotId(timeSlot.getId()) > 0;
            case "Limousine":
                return limousineReservationRepo.getCountByTimeSlotId(timeSlot.getId()) > 0;
            case "Stylist":
                return stylistReservationRepo.getCountByTimeSlotId(timeSlot.getId()) > 0;
            default:
                return false;
        }
    }
}
