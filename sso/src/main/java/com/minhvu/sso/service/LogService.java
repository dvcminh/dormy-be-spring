package com.minhvu.sso.service;

import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.model.LogDto;
import com.minhvu.sso.dto.response.page.PageData;
import com.minhvu.sso.dto.response.page.PageLink;
import com.minhvu.sso.model.enums.ActionStatus;
import com.minhvu.sso.model.enums.ActionType;

import java.util.UUID;

public interface LogService {
    LogDto save(LogDto logDto, AppUserDto currentUser);

    LogDto save(LogDto logDto, UUID currentUserId);

    PageData<LogDto> findLogs(
            PageLink pageLink,
            UUID userId,
            ActionStatus actionStatus,
            ActionType actionType,
            Long createdAtStartTs,
            Long createdAtEndTs,
            Boolean isSearchMatchCase
    );
}
