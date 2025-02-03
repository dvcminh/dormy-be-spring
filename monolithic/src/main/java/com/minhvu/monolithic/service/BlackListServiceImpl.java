package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.BlackList;
import com.minhvu.monolithic.exception.NotFoundException;
import com.minhvu.monolithic.repository.BlackListRepository;
import com.minhvu.monolithic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blocklistRepository;
    private final UserRepository userRepository;

    @Transactional
    public void blockUser(UUID blockerId, UUID blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new IllegalArgumentException("You cannot block yourself.");
        }

        AppUser blocker = userRepository.findById(blockerId)
                .orElseThrow(() -> new NotFoundException("Blocker not found."));
        AppUser blocked = userRepository.findById(blockedId)
                .orElseThrow(() -> new NotFoundException("Blocked user not found."));

        if (blocklistRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            throw new IllegalStateException("User is already blocked.");
        }

        BlackList blocklist = BlackList.builder()
                .blocker(blocker)
                .blocked(blocked)
                .blockedAt(LocalDateTime.now())
                .build();

        blocklistRepository.save(blocklist);
    }

    @Transactional
    public void unblockUser(UUID blockerId, UUID blockedId) {
        AppUser blocker = userRepository.findById(blockerId)
                .orElseThrow(() -> new NotFoundException("Blocker not found."));
        AppUser blocked = userRepository.findById(blockedId)
                .orElseThrow(() -> new NotFoundException("Blocked user not found."));

        if (!blocklistRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            throw new IllegalStateException("User is not blocked.");
        }

        blocklistRepository.deleteByBlockerAndBlocked(blocker, blocked);
    }

    public boolean isBlocked(UUID blockerId, UUID blockedId) {
        return blocklistRepository.existsByBlockerAndBlocked(
                userRepository.findById(blockerId).orElseThrow(() -> new NotFoundException("Blocker not found.")),
                userRepository.findById(blockedId).orElseThrow(() -> new NotFoundException("Blocked user not found."))
        );
    }

    @Override
    public PageData<BlackList> getBlockListByBlocker(UUID blockerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new PageData<>(blocklistRepository.findAllByBlockerId(blockerId, pageable));
    }
}
