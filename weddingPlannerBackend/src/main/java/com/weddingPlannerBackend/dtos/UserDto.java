package com.weddingPlannerBackend.dtos;

import com.weddingPlannerBackend.model.Reservation;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private String name;
    private String address;
    private String email;
    private String password;
    private Set<Reservation> userReservations;
    private boolean isVerified;
}
