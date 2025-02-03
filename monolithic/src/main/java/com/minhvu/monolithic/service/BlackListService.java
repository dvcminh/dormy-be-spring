package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.entity.BlackList;

import java.util.UUID;

public interface BlackListService {
    void blockUser(UUID blockerId, UUID blockedId);

    void unblockUser(UUID blockerId, UUID blockedId);

    boolean isBlocked(UUID blockerId, UUID blockedId);

    PageData<BlackList> getBlockListByBlocker(UUID blockerId, int page, int size);
}
