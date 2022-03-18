package com.weddingPlannerBackend.mappers;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.weddingPlannerBackend.dtos.UserDto;
import com.weddingPlannerBackend.model.User;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());

        return userDto;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        if (userDto.getPassword() != null)
            user.setPassword(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()));
          
        return user;
    }
}
