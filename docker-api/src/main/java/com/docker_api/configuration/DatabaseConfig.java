package com.docker_api.configuration;

import com.docker_api.model.VideoGame;
import com.docker_api.repository.VideoGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {
    private final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    private final VideoGameRepository videoGameRepository;

    public DatabaseConfig(VideoGameRepository videoGameRepository){
        this.videoGameRepository = videoGameRepository;
    }

    @Bean
    CommandLineRunner initDatabase(){
        return args -> {
            VideoGame halo = VideoGame.builder()
                    .name("Halo Infinite")
                    .price(649.00)
                    .platform("Xbox")
                    .build();

            VideoGame assassinsCreed = VideoGame.builder()
                    .name("Assassin's Creed 2")
                    .price(449.00)
                    .platform("Xbox")
                    .build();

            VideoGame ninjaGaiden = VideoGame.builder()
                    .name("Ninja Gaiden 2")
                    .price(449.00)
                    .platform("Xbox")
                    .build();

            VideoGame lol = VideoGame.builder()
                    .name("League of Legends")
                    .price(149.90)
                    .platform("PC")
                    .build();

            log.info("Created: " + videoGameRepository.save(halo));
            log.info("Created: " + videoGameRepository.save(assassinsCreed));
            log.info("Created: " + videoGameRepository.save(ninjaGaiden));
            log.info("Created: " + videoGameRepository.save(lol));
        };
    }
}
