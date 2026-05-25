package com.outsera.goldenraspberry.infrastructure.csv;

import com.outsera.goldenraspberry.domain.model.Movie;
import com.outsera.goldenraspberry.domain.port.out.MovieRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CsvDataLoader implements ApplicationRunner {

    private final MovieRepositoryPort movieRepository;
    private final CsvMovieParser parser;
    private final Resource csvResource;

    public CsvDataLoader(MovieRepositoryPort movieRepository, CsvMovieParser parser,
                         @Value("${csv.file.path}") Resource csvResource) {
        this.movieRepository = movieRepository;
        this.parser = parser;
        this.csvResource = csvResource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Movie> movies = parser.parse(csvResource.getInputStream());
        movieRepository.saveAll(movies);
        log.info("Loaded {} movies from CSV", movies.size());
    }
}
