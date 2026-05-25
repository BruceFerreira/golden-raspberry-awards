package com.outsera.goldenraspberry.adapter.persistence.jpa;

import com.outsera.goldenraspberry.adapter.persistence.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, Long> {
    List<MovieEntity> findByWinnerTrue();
}
