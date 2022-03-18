package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.StylistActivityDto;
import com.weddingPlannerBackend.dtos.WeddingHallActivityDto;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.StylistActivity;
import com.weddingPlannerBackend.model.WeddingHallActivity;

import java.util.List;

public interface IActivity {
    LimousineActivity createLimousineActivity(LimousineActivityDto activityDto);

    Boolean editLimousineActivity(LimousineActivityDto activityDto);

    WeddingHallActivity createWeddingHallActivity(WeddingHallActivityDto activityDto);

    Boolean editWeddingHallActivity(WeddingHallActivityDto activityDto);

    StylistActivity createStylistActivity(StylistActivityDto activityDto);

    Boolean editStylistActivity(StylistActivityDto activityDto);

    List<LimousineActivityDto> getAllLimousineActivities(double min_price, double max_price, String provider);

    List<WeddingHallActivityDto> getAllWeddingHallActivities(double min_price, double max_price, String provider);

    List<StylistActivityDto> getAllStylistActivities(double min_price, double max_price, String provider);

    List<LimousineActivityDto> getProviderLimousineActivities(Provider provider);

    List<StylistActivityDto> getProviderStylistActivities(Provider provider);

    List<WeddingHallActivityDto> getProviderWeddingHallActivities(Provider provider);

    LimousineActivity getLimousineActivity(long Id);

    WeddingHallActivity getWeddingHallActivity(long Id);

    StylistActivity getStylistActivity(long Id);

    void saveLimousineActivity(LimousineActivity activity);

    void saveWeddingHallActivity(WeddingHallActivity activity);

    void saveStylistActivity(StylistActivity activity);
}
