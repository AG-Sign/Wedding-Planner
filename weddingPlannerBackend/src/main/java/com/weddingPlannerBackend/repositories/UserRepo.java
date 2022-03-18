package com.weddingPlannerBackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingPlannerBackend.model.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

}
