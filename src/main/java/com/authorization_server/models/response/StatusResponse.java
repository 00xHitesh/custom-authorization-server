package com.authorization_server.models.response;

import lombok.Data;

@Data
public class StatusResponse {
    private boolean isSuccess;
    private String message;
    private Object response;
}
