package com.outsera.goldenraspberry.adapter.persistence;

import com.outsera.goldenraspberry.adapter.persistence.jpa.MovieJpaRepository;
import com.outsera.goldenraspberry.adapter.persistence.mapper.MoviePersistenceMapper;
import com.outsera.goldenraspberry.domain.model.Movie;
import com.outsera.goldenraspberry.domain.port.out.MovieRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieRepositoryAdapter implements MovieRepositoryPort {

    private final MovieJpaRepository jpaRepository;
    private final MoviePersistenceMapper mapper;

    @Override
    public void saveAll(List<Movie> movies) {
        jpaRepository.saveAll(movies.stream().map(mapper::toEntity).toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findAllWinners() {
        return jpaRepository.findByWinnerTrue().stream().map(mapper::toDomain).toList();
    }

}
