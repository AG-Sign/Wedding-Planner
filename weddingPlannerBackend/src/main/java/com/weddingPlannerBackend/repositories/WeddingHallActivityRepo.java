package com.weddingPlannerBackend.repositories;

import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.WeddingHallActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeddingHallActivityRepo extends JpaRepository<WeddingHallActivity, Long> {
    WeddingHallActivity findById(long Id);
    List<WeddingHallActivity> findByProvider(Provider provider);
    List<WeddingHallActivity> findByPriceBetween(double min, double max);
}
