package com.weddingPlannerBackend.mappers;

import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.TimeSlot;
import com.weddingPlannerBackend.repositories.ProviderRepo;
import com.weddingPlannerBackend.repositories.TimeSlotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class LimousineActivityMapper {

    @Autowired
    private ProviderMapper providerMapper;
    @Autowired
    private TimeSlotMapper timeSlotMapper;
    @Autowired
    private ProviderRepo providerRepo;
    @Autowired
    private TimeSlotRepo timeSlotRepo;

    public LimousineActivityDto toDto(LimousineActivity activity) {
        if (activity == null)
            return null;

        LimousineActivityDto activityDto = new LimousineActivityDto();
        activityDto.setId(activity.getId());
        activityDto.setCarType(activity.getCarType());
        activityDto.setPrice(activity.getPrice());
        activityDto.setImg(activity.getImg());
        activityDto.setProvider(providerMapper.toDto(activity.getProvider()));
        activityDto.setTimeslots(new ArrayList<TimeSlotDto>());
        List<TimeSlot> timeSlots = timeSlotRepo.findByActivityTypeAndActivityId("Limousine",
                                                                                activity.getId());
        for (TimeSlot slot: timeSlots) {
            activityDto.getTimeslots().add(timeSlotMapper.toDto(slot));
        }

        return activityDto;
    }

    public LimousineActivity fromDto(LimousineActivityDto limousineActivityDto) {
        LimousineActivity activity = new LimousineActivity();

        activity.setId(limousineActivityDto.getId());
        activity.setCarType(limousineActivityDto.getCarType());
        activity.setPrice(limousineActivityDto.getPrice());
        activity.setImg(limousineActivityDto.getImg());
        Optional<Provider> providerOptional = providerRepo.findById(limousineActivityDto.getProvider().getEmail());
        providerOptional.ifPresent(activity::setProvider);
        return activity;
    }
}
