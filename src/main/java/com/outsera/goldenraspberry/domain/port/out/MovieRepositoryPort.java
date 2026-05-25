package com.outsera.goldenraspberry.domain.port.out;

import com.outsera.goldenraspberry.domain.model.Movie;

import java.util.List;

public interface MovieRepositoryPort {
    void saveAll(List<Movie> movies);
    List<Movie> findAllWinners();
}
