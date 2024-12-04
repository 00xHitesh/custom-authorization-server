package com.authorization_server.controller;

import com.authorization_server.security.CustomOidcUser;
import com.authorization_server.security.CustomUserDetails;
import com.authorization_server.service.UiService;
import com.authorization_server.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UiController {

    @Autowired
    UiController(UiService uiService) {
        this.uiService = uiService;
    }

    @Autowired
    UiService uiService;

    @GetMapping("/api/initials")
    ResponseEntity<String> getInitials(@AuthenticationPrincipal UserDetails userDetails) {

        String initials = null;
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            if (customUserDetails.getFirstName() != null && customUserDetails.getLastName() != null)
                initials = String.format("%s%s", customUserDetails.getFirstName().charAt(0), customUserDetails.getLastName().charAt(0)).toUpperCase();
            else {
                String email = customUserDetails.getUsername();
                initials = uiService.extractInitials(email);
            }
        } else if (userDetails instanceof CustomOidcUser customOidcUser && customOidcUser.getPreferredUsername() != null) {
            String email = customOidcUser.getIdToken().getClaim(Constants.getEmailClaim());
            initials = uiService.extractInitials(email);
        }
        return ResponseEntity.status(HttpStatus.OK).body(initials);
    }

}
