package com.authorization_server.service;

import com.authorization_server.entity.User;
import com.authorization_server.exception.custom.InvalidPasswordException;
import com.authorization_server.exception.custom.UserNotFoundException;
import com.authorization_server.models.password.ChangePasswordDto;
import com.authorization_server.models.response.StatusResponse;
import com.authorization_server.models.users.UserDto;
import com.authorization_server.repository.RoleRepository;
import com.authorization_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private final Environment environment;
    @Autowired
    PasswordEncoder passwordEncoder;


    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    @Override
    public User getByUsername(String username) {

        if (!StringUtils.hasText(username)) {
            return null;
        }

        Optional<User> userOptional = userRepository.findByEmailAndActive(username, 1);
        return userOptional.orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }

        Optional<User> userOptional = userRepository.findByEmailAndActive(email, 1);
        return userOptional.orElse(null);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setFederated(false);
        userDto.setActive(true);

        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());

        User user;
        if (userOptional.isEmpty()) {
            user = userDto.toEntity(roleRepository);
            user.password(getAndDispatchPassword(userDto.getEmail()));
        } else {
            user = userOptional.get();
            //@formatter:off
            user
                .roles(new HashSet<>(roleRepository.findByRoleIdIn(userDto.getRoles())))
                .emailVerified(userDto.getEmailVerified())
                .active(userDto.getActive())
                .avatarUrl(userDto.getAvatarUrl())
                .firstName(userDto.getFirstName())
                .middleName(userDto.getMiddleName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .locale(userDto.getLocale())
                .federated(userDto.getFederated())
                .build();
            //@formatter:off
        }
        userRepository.save(user);
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByUserIdAndActive(userDto.getUserId(), 1);
        if (userOptional.isEmpty()) throw new UserNotFoundException(environment.getProperty("USER_SERVICE.USER_NOT_FOUND"));
        User user = userOptional.get();
        //@formatter:off
        user
            .roles(new HashSet<>(roleRepository.findByRoleIdIn(userDto.getRoles())))
            .emailVerified(userDto.getEmailVerified())
            .active(userDto.getActive())
            .avatarUrl(userDto.getAvatarUrl())
            .firstName(userDto.getFirstName())
            .middleName(userDto.getMiddleName())
            .lastName(userDto.getLastName())
            .username(userDto.getUsername())
            .locale(userDto.getLocale())
            .federated(userDto.getFederated())
            .build();
        //@formatter:on
        userRepository.save(user);
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto findByEmail(String emailId) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(emailId);
        if (user.isEmpty()) throw new UserNotFoundException(environment.getProperty("USER_SERVICE.USER_NOT_FOUND"));
        return UserDto.fromEntity(user.get());
    }

    @Override
    public UserDto deleteByEmail(String emailId) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmailAndActive(emailId, 1);
        if (user.isEmpty()) throw new UserNotFoundException(environment.getProperty("USER_SERVICE.USER_NOT_FOUND"));
        UserDto userDto = UserDto.fromEntity(user.get());
        this.setUserStatus(emailId, false);
        return userDto;
    }

    @Override
    public StatusResponse changePassword(Integer userId, ChangePasswordDto changePasswordDto) throws UserNotFoundException, InvalidPasswordException {

        Optional<User> userOptional = userRepository.findByUserIdAndActiveAndFederated(userId, 1, 1);
        if (userOptional.isEmpty()) throw new UserNotFoundException(environment.getProperty("USER_SERVICE.USER_NOT_FOUND"));
        User user = userOptional.get();
        if (user.getPassword() == null) throw new InvalidPasswordException(environment.getProperty("USER_SERVICE.INVALID_ACTION"));
        boolean passwordMatched = passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword());
        if (!passwordMatched) throw new InvalidPasswordException(environment.getProperty("USER_SERVICE.INCORRECT_PASSWORD"));

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(changePasswordDto.getNewPassword());

        if (!matcher.matches()) throw new InvalidPasswordException(environment.getProperty("USER_SERVICE.INVALID_PASSWORD"));

        user.password(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        userRepository.save(user);
        StatusResponse response = new StatusResponse();
        response.setSuccess(true);
        response.setMessage(environment.getProperty("USER_SERVICE.PASSWORD_CHANGED"));
        return response;
    }

    @Override
    public StatusResponse setUserStatus(String emailId, Boolean isActive) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(emailId);
        if (userOptional.isEmpty()) throw new UserNotFoundException(environment.getProperty("USER_SERVICE.USER_NOT_FOUND"));

        User user = userOptional.get();
        user.active(isActive);

        userRepository.save(user);
        StatusResponse response = new StatusResponse();
        response.setSuccess(true);

        String message = Boolean.TRUE.equals(isActive) ? environment.getProperty("USER_SERVICE.USER_ACTIVATED") : environment.getProperty("USER_SERVICE.USER_DEACTIVATED");
        response.setMessage(message);
        return response;
    }

    private String getAndDispatchPassword(String emailId)
    {
        return passwordEncoder.encode("Test@123");
    }
}
