package com.docker_api.advice;

import com.docker_api.exception.VideoGameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VideoGameControllerAdvice {

    @ExceptionHandler(VideoGameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundExceptionHandler(VideoGameNotFoundException ex){
        return ex.getMessage();
    }
}
