package com.docker_api.service;

import com.docker_api.exception.VideoGameNotFoundException;
import com.docker_api.model.VideoGame;
import com.docker_api.repository.VideoGameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoGameService {

    private final VideoGameRepository videoGameRepository;

    public VideoGameService(VideoGameRepository videoGameRepository){
        this.videoGameRepository = videoGameRepository;
    }

    public VideoGame findById(Long id){

        return videoGameRepository.findById(id)
                .orElseThrow(() -> new VideoGameNotFoundException(id));
    }

    public List<VideoGame> findByNameContains(String name){
        return videoGameRepository.findByNameContains(name);
    }

    public List<VideoGame> findAll(){
        return videoGameRepository.findAll();
    }

    public VideoGame save(VideoGame videoGame){

        return videoGameRepository.save(videoGame);
    }
}
