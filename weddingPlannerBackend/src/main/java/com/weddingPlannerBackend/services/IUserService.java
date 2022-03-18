package com.weddingPlannerBackend.services;

import com.weddingPlannerBackend.dtos.UserDto;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.model.VerificationToken;

public interface IUserService {

    User registerNewUserAccount(UserDto userDto);

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

}
