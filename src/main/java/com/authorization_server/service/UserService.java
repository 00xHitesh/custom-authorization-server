package com.authorization_server.service;

import com.authorization_server.entity.User;
import com.authorization_server.exception.custom.InvalidPasswordException;
import com.authorization_server.exception.custom.UserNotFoundException;
import com.authorization_server.models.password.ChangePasswordDto;
import com.authorization_server.models.response.StatusResponse;
import com.authorization_server.models.users.UserDto;
public interface UserService {
    User getByUsername(String username);
    User getByEmail(String email);
    User save(User user);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto) throws UserNotFoundException;

    UserDto findByEmail(String emailId) throws UserNotFoundException;

    UserDto deleteByEmail(String emailId) throws UserNotFoundException;

    StatusResponse changePassword(Integer userId, ChangePasswordDto changePasswordDto) throws UserNotFoundException, InvalidPasswordException;

    StatusResponse setUserStatus(String emailId, Boolean isActive) throws UserNotFoundException;
}
