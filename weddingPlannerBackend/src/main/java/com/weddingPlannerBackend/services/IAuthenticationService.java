package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.model.AuthenticationToken;

public interface IAuthenticationService {

    AuthenticationToken getByToken(String token);

    String createToken(String email, String password, String type);

    boolean delToken(String token);

    boolean isAuthenticated(String token);

}
