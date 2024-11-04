package com.docker_api.repository;

import com.docker_api.model.VideoGame;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoGameRepository extends ListCrudRepository<VideoGame, Long> {
    List<VideoGame> findByNameContains(String name);
}
