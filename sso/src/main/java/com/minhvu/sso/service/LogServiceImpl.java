package com.minhvu.sso.service;

import com.minhvu.sso.dto.mapper.LogMapper;
import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.model.LogDto;
import com.minhvu.sso.dto.response.page.PageData;
import com.minhvu.sso.dto.response.page.PageLink;
import com.minhvu.sso.exception.BadRequestException;
import com.minhvu.sso.model.Log;
import com.minhvu.sso.model.enums.ActionStatus;
import com.minhvu.sso.model.enums.ActionType;
import com.minhvu.sso.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogMapper logMapper;
    private final LogRepository logRepository;

    @Override
    public LogDto save(LogDto logDto, AppUserDto currentUser) {
        Log log = new Log();
        BeanUtils.copyProperties(logDto, log, "createdAt", "createdBy", "actionData");
        log.setCreatedBy(currentUser == null ? null : currentUser.getId());
        log.setActionData(logDto.getActionData().toString());

        Log savedLog = logRepository.save(log);

        return logMapper.toDto(savedLog);
    }

    @Override
    public LogDto save(LogDto logDto, UUID currentUserId) {
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(currentUserId);
        return save(logDto, appUserDto);
    }

    @Override
    public PageData<LogDto> findLogs(
            PageLink pageLink,
            UUID userId, ActionStatus actionStatus,
            ActionType actionType,
            Long createdAtStartTs,
            Long createdAtEndTs,
            Boolean isSearchMatchCase
    ) {
        Pageable pageable = PageRequest.of(pageLink.getPage(), pageLink.getPageSize(), pageLink.toSort(pageLink.getSortOrder()));
        isTimeStampValid(createdAtStartTs, createdAtEndTs);

        String searchText = Objects.toString(pageLink.getSearchText(), "");

        if (searchText != null) {
            searchText = escapeSearchText(searchText);
        }

        searchText =  isSearchMatchCase ? searchText : removeAccent(searchText.toLowerCase());


        Page<Log> logPage = logRepository.findLogs(
                searchText,
                isSearchMatchCase,
                userId,
                actionStatus,
                actionType,
                convertTimestampToDateTime(createdAtStartTs),
                convertTimestampToDateTime(createdAtEndTs),
                pageable
        );

        Page<LogDto> logDtoList = logPage.map(logMapper::toDto);
        return new PageData<>(logDtoList);
    }

    private String escapeSearchText(String searchText) {
        return searchText.replace("%", "\\%")
                .replace("_", "\\_")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("^", "\\^");
    }

    private void isTimeStampValid(Long startTs, Long endTs) {
        if (startTs != null && endTs != null) {
            if (!(startTs >= 0 && endTs >= 0 && startTs <= endTs)) {
                throw new BadRequestException("Start time and end time must be valid");
            }
        }
    }

    private LocalDateTime convertTimestampToDateTime(Long timestamp) {
        return timestamp != null ?
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                : null;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d");
    }
}
