package com.docker_api.controller;

import com.docker_api.model.VideoGame;
import com.docker_api.service.VideoGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/videogames")
public class VideoGameController {

    private final VideoGameService videoGameService;

    public VideoGameController(VideoGameService videoGameService){
        this.videoGameService = videoGameService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VideoGame> findById(@PathVariable Long id){
        VideoGame videoGame = videoGameService.findById(id);

        return ResponseEntity.ok(videoGame);
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<VideoGame>> findByName(@RequestParam("name") String name){
        List<VideoGame> videoGames = videoGameService.findByNameContains(name);

        return ResponseEntity.ok(videoGames);
    }

    @GetMapping
    public ResponseEntity<List<VideoGame>> findAll(){
        List<VideoGame> videoGames = videoGameService.findAll();

        return ResponseEntity.ok(videoGames);
    }

    @PostMapping
    public ResponseEntity<VideoGame> saveVideoGame(@RequestBody VideoGame videoGame,
                                                   UriComponentsBuilder ucb){
        VideoGame newVideoGame = videoGameService.save(videoGame);

        URI savedVideoGame = ucb
                .path("/api/v1/videogames/{id}")
                .buildAndExpand(newVideoGame.getId())
                .toUri();

        return ResponseEntity.created(savedVideoGame).build();
    }

}
