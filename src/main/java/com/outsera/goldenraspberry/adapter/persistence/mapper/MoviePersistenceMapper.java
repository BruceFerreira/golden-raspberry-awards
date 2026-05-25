package com.outsera.goldenraspberry.adapter.persistence.mapper;

import com.outsera.goldenraspberry.adapter.persistence.entity.MovieEntity;
import com.outsera.goldenraspberry.domain.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MoviePersistenceMapper {

    public Movie toDomain(MovieEntity entity) {
        return new Movie(
                entity.getId(),
                entity.getYear(),
                entity.getTitle(),
                entity.getStudios(),
                entity.getProducers(),
                Boolean.TRUE.equals(entity.getWinner())
        );
    }

    public MovieEntity toEntity(Movie movie) {
        return MovieEntity.builder()
                .year(movie.year())
                .title(movie.title())
                .studios(movie.studios())
                .producers(movie.producers())
                .winner(movie.winner())
                .build();
    }
}
