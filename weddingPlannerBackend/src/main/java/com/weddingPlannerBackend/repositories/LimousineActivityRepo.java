package com.weddingPlannerBackend.repositories;

import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimousineActivityRepo extends JpaRepository<LimousineActivity, Long> {
    LimousineActivity findById(long Id);
    List<LimousineActivity> findByProvider(Provider provider);
    List<LimousineActivity> findByPriceBetween(double min, double max);
}
