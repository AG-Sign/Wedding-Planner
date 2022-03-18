package com.weddingPlannerBackend.dtos;

import lombok.Data;

@Data
public class ProviderDto {
    private String name;
    private String address;
    private String email;
    private String password;
    private boolean isVerified;
}
