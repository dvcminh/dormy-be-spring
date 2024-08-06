package com.minhvu.notification.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.notification.dto.mapper.AppUserMapper;
import com.minhvu.notification.dto.model.AppUserDto;
import com.minhvu.notification.dto.page.PageLink;
import com.minhvu.notification.exception.BadRequestException;
import com.minhvu.notification.exception.UnauthorizedException;
import com.minhvu.notification.model.AppUser;
import com.minhvu.notification.repository.AppUserRepository;
import com.minhvu.notification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.http.HttpRequest;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public abstract class BaseController {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserService userService;

    public PageLink createPageLink(int page, int pageSize) {
        return new PageLink(page, pageSize);
    }
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppUserMapper appUserMapper;
    protected AppUserDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser currentUser = appUserRepository.findByUsername(username).get();
        if (currentUser == null) {
            throw new BadRequestException("You aren't authorized to perform this operation.");
        }
        return appUserMapper.toDto(currentUser);
    }

    Map<String, Object> parseJwt(String token) throws JsonProcessingException {
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException("Lost token");
        }
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, Object> map = mapper.readValue(payload, Map.class);
        return map;
    }
}
