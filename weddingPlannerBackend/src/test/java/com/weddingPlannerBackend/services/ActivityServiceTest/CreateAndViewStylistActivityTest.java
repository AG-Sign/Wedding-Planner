package com.weddingPlannerBackend.services.ActivityServiceTest;

import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.dtos.StylistActivityDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.StylistActivity;
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
public class CreateAndViewStylistActivityTest {
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
    public void validStylistActivity() {
        // Valid Activity Creation
        StylistActivityDto stylistActivityDto = new StylistActivityDto();
        stylistActivityDto.setDescription("Hair dresser");
        stylistActivityDto.setPrice(12000.75);
        stylistActivityDto.setProvider(providerMapper.toDto(provider));
        StylistActivity stylistActivity = activityService.createStylistActivity(stylistActivityDto);
        Assertions.assertNotNull(stylistActivity);
        Assertions.assertNotNull(activityService.getStylistActivity(stylistActivity.getId()));
    }

    @Test
    public void invalidStylistActivityPrice() {
        // Price must be > 500
        StylistActivityDto stylistActivityDto = new StylistActivityDto();
        stylistActivityDto.setDescription("Hair dresser");
        stylistActivityDto.setPrice(100.75);
        stylistActivityDto.setProvider(providerMapper.toDto(provider));
        Assertions.assertNull(activityService.createStylistActivity(stylistActivityDto));
    }

    @Test
    public void invalidStylistActivityDescription() {
        // Stylist description must be present
        StylistActivityDto stylistActivityDto = new StylistActivityDto();
        stylistActivityDto.setDescription(null);
        stylistActivityDto.setPrice(1200.75);
        stylistActivityDto.setProvider(providerMapper.toDto(provider));
        Assertions.assertNull(activityService.createStylistActivity(stylistActivityDto));
    }

    @Test
    public void invalidStylistActivityProvider() {
        // Activity provider must be present
        StylistActivityDto stylistActivityDto = new StylistActivityDto();
        stylistActivityDto.setDescription("Hair Dresser");
        stylistActivityDto.setPrice(1200.75);
        stylistActivityDto.setProvider(null);
        Assertions.assertNull(activityService.createStylistActivity(stylistActivityDto));
    }
}

