package com.docker_api.exception;

public class VideoGameNotFoundException extends RuntimeException{

    public VideoGameNotFoundException(long id){
        super("Couldn't find any game with id: " + id);
    }
}
