package com.weddingPlannerBackend.repositories;

import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.StylistActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StylistActivityRepo extends JpaRepository<StylistActivity, Long> {
    StylistActivity findById(long Id);
    List<StylistActivity> findByProvider(Provider provider);
    List<StylistActivity> findByPriceBetween(double min, double max);
}
