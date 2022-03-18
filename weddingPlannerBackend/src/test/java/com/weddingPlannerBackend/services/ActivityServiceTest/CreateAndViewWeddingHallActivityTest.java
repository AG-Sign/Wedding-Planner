package com.weddingPlannerBackend.services.ActivityServiceTest;

import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.dtos.WeddingHallActivityDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.WeddingHallActivity;
import com.weddingPlannerBackend.services.ActivityService;
import com.weddingPlannerBackend.services.ProviderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateAndViewWeddingHallActivityTest {
    @Autowired
    private ActivityService activityService;
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
    }

    @Test
    public void validWeddingHallActivity() {
        // Valid Activity Creation
        WeddingHallActivityDto weddingHallActivityDto = new WeddingHallActivityDto();
        weddingHallActivityDto.setNumberOfGuests(250);
        weddingHallActivityDto.setPrice(5000);
        weddingHallActivityDto.setHasRoom(true);
        weddingHallActivityDto.setProvider(providerMapper.toDto(provider));
        WeddingHallActivity weddingHallActivity = activityService.createWeddingHallActivity(weddingHallActivityDto);
        Assertions.assertNotNull(weddingHallActivity);
        Assertions.assertNotNull(activityService.getWeddingHallActivity(weddingHallActivity.getId()));
    }

    @Test
    public void invalidWeddingHallActivityPrice() {
        // Price must be > 4000
        WeddingHallActivityDto weddingHallActivityDto = new WeddingHallActivityDto();
        weddingHallActivityDto.setNumberOfGuests(250);
        weddingHallActivityDto.setPrice(1000);
        weddingHallActivityDto.setHasRoom(true);
        weddingHallActivityDto.setProvider(providerMapper.toDto(provider));
        Assertions.assertNull(activityService.createWeddingHallActivity(weddingHallActivityDto));
    }

    @Test
    public void invalidWeddingHallActivityNumberOfGuests() {
        // Number of guests must be > 50
        WeddingHallActivityDto weddingHallActivityDto = new WeddingHallActivityDto();
        weddingHallActivityDto.setNumberOfGuests(25);
        weddingHallActivityDto.setPrice(5000);
        weddingHallActivityDto.setHasRoom(true);
        weddingHallActivityDto.setProvider(providerMapper.toDto(provider));
        Assertions.assertNull(activityService.createWeddingHallActivity(weddingHallActivityDto));
    }

    @Test
    public void invalidWeddingHallActivityProvider() {
        // Activity provider must be present
        WeddingHallActivityDto weddingHallActivityDto = new WeddingHallActivityDto();
        weddingHallActivityDto.setNumberOfGuests(250);
        weddingHallActivityDto.setPrice(5000);
        weddingHallActivityDto.setHasRoom(true);
        weddingHallActivityDto.setProvider(null);
        Assertions.assertNull(activityService.createWeddingHallActivity(weddingHallActivityDto));
    }
}
