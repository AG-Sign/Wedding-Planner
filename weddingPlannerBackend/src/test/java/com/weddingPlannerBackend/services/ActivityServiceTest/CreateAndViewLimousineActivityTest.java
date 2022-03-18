package com.weddingPlannerBackend.services.ActivityServiceTest;

import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.ProviderDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.services.ActivityService;
import com.weddingPlannerBackend.services.ProviderService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateAndViewLimousineActivityTest {
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
    public void validLimousineActivity() {
        // Valid Activity Creation
        LimousineActivityDto limousineActivityDto = new LimousineActivityDto();
        limousineActivityDto.setCarType("Mazda 3");
        limousineActivityDto.setPrice(1200.2);
        limousineActivityDto.setProvider(providerMapper.toDto(provider));
        LimousineActivity limousineActivity = activityService.createLimousineActivity(limousineActivityDto);
        Assertions.assertNotNull(limousineActivity);
        Assertions.assertNotNull(activityService.getLimousineActivity(limousineActivity.getId()));
    }

    @Test
    public void invalidLimousineActivityPrice() {
        // Price must be > 800
        LimousineActivityDto limousineActivityDto = new LimousineActivityDto();
        limousineActivityDto.setCarType("Mazda 3");
        limousineActivityDto.setPrice(100.2);
        limousineActivityDto.setProvider(providerMapper.toDto(provider));
        Assertions.assertNull(activityService.createLimousineActivity(limousineActivityDto));
    }

    @Test
    public void invalidLimousineActivityCarType() {
        // Car type must be present
        LimousineActivityDto limousineActivityDto = new LimousineActivityDto();
        limousineActivityDto.setCarType(null);
        limousineActivityDto.setPrice(1200.2);
        limousineActivityDto.setProvider(providerMapper.toDto(provider));
        Assertions.assertNull(activityService.createLimousineActivity(limousineActivityDto));
    }

    @Test
    public void invalidLimousineActivityProvider() {
        // Activity provider must be present
        LimousineActivityDto limousineActivityDto = new LimousineActivityDto();
        limousineActivityDto.setCarType("Dodge");
        limousineActivityDto.setPrice(1200.2);
        limousineActivityDto.setProvider(null);
        Assertions.assertNull(activityService.createLimousineActivity(limousineActivityDto));
    }
}
