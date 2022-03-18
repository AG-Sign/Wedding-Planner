package com.weddingPlannerBackend.repositories;

import com.weddingPlannerBackend.model.AuthenticationToken;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationTokenRepo extends JpaRepository<AuthenticationToken, String> {
    Optional<AuthenticationToken> findByUser(User user);
    Optional<AuthenticationToken> findByProvider(Provider provider);

    AuthenticationToken findByToken(String token);
}
