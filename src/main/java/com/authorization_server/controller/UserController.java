package com.authorization_server.controller;

import com.authorization_server.exception.custom.InvalidPasswordException;
import com.authorization_server.exception.custom.UserNotFoundException;
import com.authorization_server.models.password.ChangePasswordDto;
import com.authorization_server.models.response.StatusResponse;
import com.authorization_server.models.users.UserDto;
import com.authorization_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param userDto : User object to create the user
     * @return created user
     */
    @PostMapping("/users")
    ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        if (userDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        UserDto userResponse = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/users")
    ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) throws UserNotFoundException {
        if (userDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        UserDto user = userService.updateUser(userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{emailId}")
    ResponseEntity<UserDto> getUser(@PathVariable("emailId") String emailId) throws UserNotFoundException {
        if (emailId == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        UserDto userDto = userService.findByEmail(emailId);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/users/{emailId}")
    ResponseEntity<UserDto> deleteUser(@PathVariable("emailId") String emailId) throws UserNotFoundException {
        if (emailId == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        UserDto userDto = userService.deleteByEmail(emailId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/users/{emailId}/status/{active}")
    ResponseEntity<StatusResponse> setUserStatus(@PathVariable("emailId") String emailId, @PathVariable("active") Boolean isActive) throws UserNotFoundException {
        if (emailId == null || isActive == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        StatusResponse statusResponse = userService.setUserStatus(emailId, isActive);
        return ResponseEntity.ok(statusResponse);
    }

//    @PostMapping("/password/{userId}")
//    ResponseEntity<UserDto> setPassword(@PathVariable("userId") Integer userId) throws UserNotFoundException {
//        if (userId == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        UserDto userDto = userService.deleteById(userId);
//        return ResponseEntity.ok(userDto);
//    }

    @PutMapping("/password/{userId}")
    ResponseEntity<StatusResponse> changePassword(@PathVariable("userId") Integer userId, @RequestBody ChangePasswordDto changePasswordDto) throws UserNotFoundException, InvalidPasswordException {
        if (userId == null || changePasswordDto == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        StatusResponse statusResponse = userService.changePassword(userId, changePasswordDto);
        return ResponseEntity.ok(statusResponse);
    }
}
