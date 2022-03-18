package com.weddingPlannerBackend.mappers;

import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.dtos.WeddingHallActivityDto;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.TimeSlot;
import com.weddingPlannerBackend.model.WeddingHallActivity;
import com.weddingPlannerBackend.repositories.ProviderRepo;
import com.weddingPlannerBackend.repositories.TimeSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class WeddingHallActivityMapper {
    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private TimeSlotMapper timeSlotMapper;
    @Autowired
    private ProviderRepo providerRepo;
    @Autowired
    private TimeSlotRepo timeSlotRepo;

    public WeddingHallActivityDto toDto(WeddingHallActivity activity) {
        if (activity == null)
            return null;

        WeddingHallActivityDto activityDto = new WeddingHallActivityDto();
        activityDto.setId(activity.getId());
        activityDto.setNumberOfGuests(activity.getNumberOfGuests());
        activityDto.setHasRoom(activity.isHasRoom());
        activityDto.setPrice(activity.getPrice());
        activityDto.setImg(activity.getImg());
        activityDto.setProvider(providerMapper.toDto(activity.getProvider()));
        activityDto.setTimeslots(new ArrayList<TimeSlotDto>());
        List<TimeSlot> timeSlots = timeSlotRepo.findByActivityTypeAndActivityId("WeddingHall",
                                                                                activity.getId());
        for (TimeSlot slot: timeSlots) {
            activityDto.getTimeslots().add(timeSlotMapper.toDto(slot));
        }

        return activityDto;
    }

    public WeddingHallActivity fromDto(WeddingHallActivityDto weddingHallActivityDto) {
        WeddingHallActivity activity = new WeddingHallActivity();

        activity.setId(weddingHallActivityDto.getId());
        activity.setNumberOfGuests(weddingHallActivityDto.getNumberOfGuests());
        activity.setHasRoom(weddingHallActivityDto.isHasRoom());
        activity.setPrice(weddingHallActivityDto.getPrice());
        activity.setImg(weddingHallActivityDto.getImg());
        Optional<Provider> providerOptional = providerRepo.findById(weddingHallActivityDto.getProvider().getEmail());
        providerOptional.ifPresent(activity::setProvider);

        return activity;
    }
}
