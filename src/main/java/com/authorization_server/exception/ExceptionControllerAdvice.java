package com.authorization_server.exception;

import com.authorization_server.exception.custom.UserNotFoundException;
import com.authorization_server.models.response.StatusResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StatusResponse> responseStatusExceptionHandler(UserNotFoundException e,
                                                                         HttpServletRequest request) {
        StatusResponse response = new StatusResponse();
        response.setSuccess(false);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
