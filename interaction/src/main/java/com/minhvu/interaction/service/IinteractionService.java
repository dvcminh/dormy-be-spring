package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.InteractionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IinteractionService {

    InteractionDto getInteractionsOfPost(Long postId);
}
