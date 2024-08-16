package com.minhvu.interaction.repository;

import com.minhvu.interaction.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IreactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByPostId(Long postId);

    @Override
    boolean existsById(Long aLong);
}
