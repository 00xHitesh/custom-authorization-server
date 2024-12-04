package com.authorization_server.models.password;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
}
