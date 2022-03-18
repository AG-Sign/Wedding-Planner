package com.weddingPlannerBackend.repositories;

import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
