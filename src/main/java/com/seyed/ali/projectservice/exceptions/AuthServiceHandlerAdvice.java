package com.seyed.ali.projectservice.exceptions;

import com.seyed.ali.projectservice.model.dto.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class AuthServiceHandlerAdvice {

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(FORBIDDEN)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        return new Result(
                false,
                FORBIDDEN,
                "No permission.",
                "ServerMessage - " + e.getMessage()
        );
    }

}