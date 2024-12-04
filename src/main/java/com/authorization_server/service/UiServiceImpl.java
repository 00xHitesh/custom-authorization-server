package com.authorization_server.service;

import com.authorization_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UiServiceImpl implements UiService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private final Environment environment;

//    public UserDto getUserData() throws UserNotFoundException {
//        Optional<User> user = userRepository.findByEmailAndActive("hitesh.narkhede@dev.com", 1);
//
//        if (user.isEmpty())
//            throw new UserNotFoundException(environment.getProperty("USER_SERVICE.USER_NOT_FOUND"));
//
//        return UserDto.fromEntity(user.get());
//    }

    public String extractInitials(String email) {
        if (email.substring(0, email.indexOf("@")).contains(".")) {
            String[] parts = email.substring(0, email.indexOf("@")).split("\\.");
            return String.format("%s%s", parts[0].charAt(0), parts[1].charAt(0)).toUpperCase();
        } else
            return email.substring(0, 2).toUpperCase();
    }

}
