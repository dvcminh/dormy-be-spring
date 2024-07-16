package com.minhvu.sso.controller;

import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.model.LogDto;
import com.minhvu.sso.dto.response.page.PageData;
import com.minhvu.sso.dto.response.page.PageLink;
import com.minhvu.sso.model.enums.ActionStatus;
import com.minhvu.sso.model.enums.ActionType;
import com.minhvu.sso.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/sso/log")
public class LogController extends BaseController {
    @Autowired
    LogService logService;

    @GetMapping
    public PageData<LogDto> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) ActionStatus actionStatus,
            @RequestParam(required = false) ActionType actionType,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Long createdAtStartTs,
            @RequestParam(required = false) Long createdAtEndTs,
            @RequestParam(defaultValue = "false") Boolean isSearchMatchCase
    ) {
        PageLink pageLink = createPageLink(
                page, pageSize, searchText, sortProperty, sortOrder
        );
        AppUserDto currentUser = getCurrentUser();
        return logService.findLogs(
                pageLink,
                userId,
                actionStatus,
                actionType,
                createdAtStartTs,
                createdAtEndTs,
                isSearchMatchCase
        );
    }
}
