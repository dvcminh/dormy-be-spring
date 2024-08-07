package com.minhvu.authservice.controller;


import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.response.page.PageLink;
import com.minhvu.authservice.dto.response.page.SortOrder;
import com.minhvu.authservice.exception.BadRequestException;
import com.minhvu.authservice.repository.AppUserRepository;
import com.minhvu.authservice.service.AuthService;
import com.minhvu.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class BaseController {

    @Autowired
    private UserService userService;

    public PageLink createPageLink(int page, int pageSize, String searchText,
                                   String sortProperty, String sortOrder) {
        if (!StringUtils.isEmpty(sortProperty)) {
            SortOrder.Direction direction = SortOrder.Direction.DESC;
            if (!StringUtils.isEmpty(sortOrder)) {
                direction = SortOrder.Direction.lookup(sortOrder.toUpperCase());
            }
            SortOrder sort = new SortOrder(sortProperty, direction);
            return new PageLink(page, pageSize, searchText, sort);
        } else {
            return new PageLink(page, pageSize, searchText);
        }
    }

    protected AppUserDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUserDto currentUser = userService.findByEmail(email);
        if (currentUser == null) {
            throw new BadRequestException("You aren't authorized to perform this operation.");
        }
        return currentUser;
    }

}
