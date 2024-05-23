package com.seyed.ali.projectservice.exceptions.handler;

import com.seyed.ali.projectservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.projectservice.model.payload.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ProjectServiceHandlerAdvice {

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Result> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(FORBIDDEN).body(new Result(
                false,
                FORBIDDEN,
                "No permission.",
                "ServerMessage - " + e.getMessage()
        ));
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Result> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new Result(
                false,
                NOT_FOUND,
                "The requested resource was not found.",
                "ServerMessage - " + e.getMessage()
        ));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Result> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new Result(
                false,
                BAD_REQUEST,
                "There was an error parsing JSON.",
                e.getMessage()
        ));
    }

}
