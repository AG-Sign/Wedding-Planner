package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.StylistActivityDto;
import com.weddingPlannerBackend.dtos.WeddingHallActivityDto;
import com.weddingPlannerBackend.mappers.LimousineActivityMapper;
import com.weddingPlannerBackend.mappers.StylistActivityMapper;
import com.weddingPlannerBackend.mappers.WeddingHallActivityMapper;
import com.weddingPlannerBackend.model.*;
import com.weddingPlannerBackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityService implements IActivity {
    @Autowired
    private LimousineActivityRepo limousineActivityRepo;
    @Autowired
    private WeddingHallActivityRepo weddingHallActivityRepo;
    @Autowired
    private StylistActivityRepo stylistActivityRepo;
    @Autowired
    private ProviderRepo providerRepo;

    @Autowired
    private LimousineActivityMapper limousineActivityMapper;
    @Autowired
    private WeddingHallActivityMapper weddingHallActivityMapper;
    @Autowired
    private StylistActivityMapper stylistActivityMapper;

    @Override
    public LimousineActivity createLimousineActivity(LimousineActivityDto activityDto) {

        LimousineActivity activity = limousineActivityMapper.fromDto(activityDto);
        if (!validateLimousine(activity))
            return null;

        return limousineActivityRepo.save(activity);
    }

    @Override
    public Boolean editLimousineActivity(LimousineActivityDto activityDto) {
        LimousineActivity activity = limousineActivityMapper.fromDto(activityDto);
        Provider provider = providerRepo.getById(activityDto.getProvider().getEmail());
        activity.setProvider(provider);
        if (!validateLimousine(activity)) return false;
        limousineActivityRepo.save(activity);
        return true;
    }


    @Override
    public WeddingHallActivity createWeddingHallActivity(WeddingHallActivityDto activityDto) {

        WeddingHallActivity activity = weddingHallActivityMapper.fromDto(activityDto);
        if (!validateWeddingHall(activity))
            return null;

        return weddingHallActivityRepo.save(activity);
    }

    @Override
    public Boolean editWeddingHallActivity(WeddingHallActivityDto activityDto) {
        WeddingHallActivity activity = weddingHallActivityMapper.fromDto(activityDto);
        Provider provider = providerRepo.getById(activityDto.getProvider().getEmail());
        activity.setProvider(provider);
        if (!validateWeddingHall(activity)) return false;
        weddingHallActivityRepo.save(activity);
        return true;
    }

    @Override
    public StylistActivity createStylistActivity(StylistActivityDto activityDto) {

        StylistActivity activity = stylistActivityMapper.fromDto(activityDto);
        if (!validateStylist(activity))
            return null;

        return stylistActivityRepo.save(activity);
    }

    @Override
    public Boolean editStylistActivity(StylistActivityDto activityDto) {
        StylistActivity activity = stylistActivityMapper.fromDto(activityDto);
        Provider provider = providerRepo.getById(activityDto.getProvider().getEmail());
        activity.setProvider(provider);
        if (!validateStylist(activity)) return false;
        stylistActivityRepo.save(activity);
        return true;
    }

    private boolean validateLimousine(LimousineActivity activity) {
        return activity.getCarType() != null && activity.getProvider() != null && activity.getPrice() > 800;
    }

    private boolean validateWeddingHall(WeddingHallActivity activity) {
        return activity.getNumberOfGuests() >  50 && activity.getProvider() != null && activity.getPrice() > 4000;
    }

    private boolean validateStylist(StylistActivity activity) {
        return activity.getDescription() != null && activity.getProvider() != null && activity.getPrice() > 500;
    }

    @Override
    public List<LimousineActivityDto> getAllLimousineActivities(double min_price, double max_price, String provider) {
        List<LimousineActivity> activities = limousineActivityRepo.findByPriceBetween(min_price, max_price);
        List<LimousineActivityDto> activityDtos = new ArrayList<LimousineActivityDto>();

        for (LimousineActivity activity: activities) {
            if (!provider.equals("") && !activity.getProvider().getName().matches(".*"+provider+".*"))
                continue;
            activityDtos.add(limousineActivityMapper.toDto(activity));
        }
        return activityDtos;
    }

    @Override
    public List<WeddingHallActivityDto> getAllWeddingHallActivities(double min_price, double max_price, String provider) {
        List<WeddingHallActivity> activities = weddingHallActivityRepo.findByPriceBetween(min_price, max_price);
        List<WeddingHallActivityDto> activityDtos = new ArrayList<WeddingHallActivityDto>();

        for (WeddingHallActivity activity: activities) {
            if (!provider.equals("") && !activity.getProvider().getName().matches(".*"+provider+".*"))
                continue;
            activityDtos.add(weddingHallActivityMapper.toDto(activity));
        }
        return activityDtos;
    }

    @Override
    public List<StylistActivityDto> getAllStylistActivities(double min_price, double max_price, String provider) {
        List<StylistActivity> activities = stylistActivityRepo.findByPriceBetween(min_price, max_price);
        List<StylistActivityDto> activityDtos = new ArrayList<StylistActivityDto>();

        for (StylistActivity activity: activities) {
            if (!provider.equals("") && !activity.getProvider().getName().matches(".*"+provider+".*"))
                continue;
            activityDtos.add(stylistActivityMapper.toDto(activity));
        }
        return activityDtos;
    }

    @Override
    public List<LimousineActivityDto> getProviderLimousineActivities(Provider provider) {
        List<LimousineActivity> activities = limousineActivityRepo.findByProvider(provider);
        List<LimousineActivityDto> activityDtos = new ArrayList<LimousineActivityDto>();

        for (LimousineActivity activity: activities) {
            activityDtos.add(limousineActivityMapper.toDto(activity));
        }
        return activityDtos;
    }

    @Override
    public List<StylistActivityDto> getProviderStylistActivities(Provider provider) {
        List<StylistActivity> activities = stylistActivityRepo.findByProvider(provider);
        List<StylistActivityDto> activityDtos = new ArrayList<>();

        for (StylistActivity activity: activities) {
            activityDtos.add(stylistActivityMapper.toDto(activity));
        }
        return activityDtos;
    }

    @Override
    public List<WeddingHallActivityDto> getProviderWeddingHallActivities(Provider provider) {
        List<WeddingHallActivity> activities = weddingHallActivityRepo.findByProvider(provider);
        List<WeddingHallActivityDto> activityDtos = new ArrayList<>();

        for (WeddingHallActivity activity: activities) {
            activityDtos.add(weddingHallActivityMapper.toDto(activity));
        }
        return activityDtos;
    }

    @Override
    public LimousineActivity getLimousineActivity(long Id) {
        return limousineActivityRepo.findById(Id);
    }

    @Override
    public WeddingHallActivity getWeddingHallActivity(long Id) {
        return weddingHallActivityRepo.findById(Id);
    }

    @Override
    public StylistActivity getStylistActivity(long Id) {
        return stylistActivityRepo.findById(Id);
    }

    @Override
    public void saveLimousineActivity(LimousineActivity activity) { limousineActivityRepo.save(activity); }
    @Override
    public void saveWeddingHallActivity(WeddingHallActivity activity) { weddingHallActivityRepo.save(activity); }
    @Override
    public void saveStylistActivity(StylistActivity activity) { stylistActivityRepo.save(activity); }

    private void overwriteWeddingHall(WeddingHallActivity from, WeddingHallActivity to) {
        to.setImg(from.getImg());
        to.setHasRoom(from.isHasRoom());
        to.setPrice(from.getPrice());
        to.setNumberOfGuests(from.getNumberOfGuests());
        weddingHallActivityRepo.save(to);
    }

    private void overwriteStylist(StylistActivity from, StylistActivity to) {
        to.setImg(from.getImg());
        to.setDescription(from.getDescription());
        to.setPrice(from.getPrice());
        stylistActivityRepo.save(to);
    }
}
