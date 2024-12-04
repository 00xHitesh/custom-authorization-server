package com.authorization_server.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClaimsOverrideData {
    private String username;
    private String userRole;
}
