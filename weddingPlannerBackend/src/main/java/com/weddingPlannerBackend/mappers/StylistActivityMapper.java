package com.weddingPlannerBackend.mappers;

import com.weddingPlannerBackend.dtos.StylistActivityDto;
import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.StylistActivity;
import com.weddingPlannerBackend.model.TimeSlot;
import com.weddingPlannerBackend.repositories.ProviderRepo;
import com.weddingPlannerBackend.repositories.TimeSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StylistActivityMapper {
    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private TimeSlotMapper timeSlotMapper;
    @Autowired
    private ProviderRepo providerRepo;
    @Autowired
    private TimeSlotRepo timeSlotRepo;

    public StylistActivityDto toDto(StylistActivity activity) {
        if (activity == null)
            return null;

        StylistActivityDto activityDto = new StylistActivityDto();
        activityDto.setId(activity.getId());
        activityDto.setDescription(activity.getDescription());
        activityDto.setPrice(activity.getPrice());
        activityDto.setImg(activity.getImg());
        activityDto.setProvider(providerMapper.toDto(activity.getProvider()));
        activityDto.setTimeslots(new ArrayList<TimeSlotDto>());
        List<TimeSlot> timeSlots = timeSlotRepo.findByActivityTypeAndActivityId("Stylist",
                                                                                activity.getId());
        for (TimeSlot slot: timeSlots) {
            activityDto.getTimeslots().add(timeSlotMapper.toDto(slot));
        }

        return activityDto;
    }

    public StylistActivity fromDto(StylistActivityDto stylistActivityDto) {
        StylistActivity activity = new StylistActivity();

        activity.setId(stylistActivityDto.getId());
        activity.setDescription(stylistActivityDto.getDescription());
        activity.setPrice(stylistActivityDto.getPrice());
        activity.setImg(stylistActivityDto.getImg());
        Optional<Provider> providerOptional = providerRepo.findById(stylistActivityDto.getProvider().getEmail());
        providerOptional.ifPresent(activity::setProvider);

        return activity;
    }
}
