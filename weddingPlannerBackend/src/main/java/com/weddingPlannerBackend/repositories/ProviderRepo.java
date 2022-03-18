package com.weddingPlannerBackend.repositories;

import com.weddingPlannerBackend.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepo extends JpaRepository<Provider, String> {
    Provider findByEmail(String email);
}
