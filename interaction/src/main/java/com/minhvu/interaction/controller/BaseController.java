package com.minhvu.interaction.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.interaction.dto.AppUserDto;
import com.minhvu.interaction.dto.page.PageLink;
import com.minhvu.interaction.dto.page.SortOrder;
import com.minhvu.interaction.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class BaseController {

    @Autowired
    UserService userService;

    ObjectMapper mapper = new ObjectMapper();

    public PageLink createPageLink(int page, int pageSize, String searchText, String sortProperty, String sortOrder) {
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

    protected AppUserDto getCurrentUser(HttpServletRequest request) {
        try {
            Map<String, String> jwt = parseJwt(request.getHeader("Authorization"));
            AppUserDto currentUser = userService.findByUserId(UUID.fromString(jwt.get("userId")));
            if (currentUser == null)
                throw new ForbiddenException("You aren't authorized to perform this operation.");
            else return currentUser;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }


    Map<String, String> parseJwt(String token) throws JsonProcessingException {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = token.split("Bearer ")[1].split("\\.");
        String header = new String(decoder.decode(parts[0]));
        String payload = new String(decoder.decode(parts[1]));
        Map<String, String> map = mapper.readValue(payload, Map.class);
        return map;
    }

}
