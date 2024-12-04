package com.authorization_server.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String timestamp;
    private Integer errorCode;
    private Integer status;
    private String message;
    private String path;
}
