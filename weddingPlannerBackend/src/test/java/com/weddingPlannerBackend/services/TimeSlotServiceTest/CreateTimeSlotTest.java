package com.weddingPlannerBackend.services.TimeSlotServiceTest;

import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.dtos.StylistActivityDto;
import com.weddingPlannerBackend.dtos.TimeSlotDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.StylistActivity;
import com.weddingPlannerBackend.model.TimeSlot;
import com.weddingPlannerBackend.services.ActivityService;
import com.weddingPlannerBackend.services.ProviderService;
import com.weddingPlannerBackend.services.TimeSlotService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateTimeSlotTest {
    @Autowired
    private ActivityService activityService;
    private StylistActivity activity;
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private ProviderService providerService;
    private Provider provider;

    @Autowired
    private ProviderMapper providerMapper;

    @Before
    public void setup() {
        ProviderDto providerDto = new ProviderDto();
        long timeKey = System.currentTimeMillis();
        providerDto.setEmail(timeKey+"testing@example.com");
        providerDto.setVerified(true);
        providerDto.setAddress("Alexandria");
        providerDto.setName("Dummy Provider");
        providerDto.setPassword("@go12354682");
        provider = providerService.registerNewProviderAccount(providerDto);
        StylistActivityDto stylistActivityDto = new StylistActivityDto();
        stylistActivityDto.setDescription("Hair dresser");
        stylistActivityDto.setPrice(12000.75);
        stylistActivityDto.setProvider(providerMapper.toDto(provider));
        activity = activityService.createStylistActivity(stylistActivityDto);
    }

    @Test
    public void validTimeSlot() {
        // Valid Activity Creation
        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setActivityType("Stylist");
        timeSlotDto.setActivityId(activity.getId());
        timeSlotDto.setStartTime("02:30");
        timeSlotDto.setEndTime("05:30");
        TimeSlot timeSlot = timeSlotService.CreateTimeSlot(timeSlotDto, provider);
        Assertions.assertNotNull(timeSlot);
    }

    @Test
    public void invalidTimeSlotInversion() {
        // Start time must be before end time
        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setActivityType("Stylist");
        timeSlotDto.setActivityId(activity.getId());
        timeSlotDto.setStartTime("05:30");
        timeSlotDto.setEndTime("02:30");
        TimeSlot timeSlot = timeSlotService.CreateTimeSlot(timeSlotDto, provider);
        Assertions.assertNull(timeSlot);
    }

    @Test
    public void invalidTimeSlotOverlap() {
        // Time slots for the same activity must not be overlapped
        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto.setActivityType("Stylist");
        timeSlotDto.setActivityId(activity.getId());
        timeSlotDto.setStartTime("02:30");
        timeSlotDto.setEndTime("05:30");
        TimeSlot timeSlot = timeSlotService.CreateTimeSlot(timeSlotDto, provider);
        Assertions.assertNotNull(timeSlot);
        timeSlotDto = new TimeSlotDto();
        timeSlotDto.setActivityType("Stylist");
        timeSlotDto.setActivityId(activity.getId());
        timeSlotDto.setStartTime("04:30");
        timeSlotDto.setEndTime("08:30");
        timeSlot = timeSlotService.CreateTimeSlot(timeSlotDto, provider);
        Assertions.assertNull(timeSlot);
    }

    @Test
    public void invalidTimeSlotNoActivity() {
        // Timeslot must be have a valid activity
        TimeSlotDto timeSlotDto = new TimeSlotDto();
        timeSlotDto = new TimeSlotDto();
        timeSlotDto.setActivityType(null);
        timeSlotDto.setActivityId(activity.getId());
        timeSlotDto.setStartTime("02:30");
        timeSlotDto.setEndTime("04:30");
        TimeSlot timeSlot = timeSlotService.CreateTimeSlot(timeSlotDto, provider);
        Assertions.assertNull(timeSlot);
    }
}


