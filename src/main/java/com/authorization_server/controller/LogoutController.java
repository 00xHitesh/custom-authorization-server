package com.authorization_server.controller;

import com.authorization_server.service.JpaOAuth2AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class LogoutController {

    @Autowired
    JpaOAuth2AuthorizationService authorizationService;

    @GetMapping("/logout/connect/front-channel/{registrationId}")
    ResponseEntity<?> frontChannelLogout(@PathVariable String registrationId, @RequestParam("sid") String sid) throws NoSuchAlgorithmException {
        authorizationService.performFrontChannelLogout(sid);
        return ResponseEntity.noContent().build();
    }

}
