package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.dto.model.NotificationUserResponseDto;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.dto.response.page.PageLink;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/notification")
@AllArgsConstructor
public class NotificationController extends BaseController {
    private final NotificationService notificationService;

    @Operation(summary = "Get All Notifications for a user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<PageData<NotificationUserResponseDto>> getNotificationUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Boolean isRead) {
        AppUser appUser = getCurrentUser();
        PageLink pageLink = createPageLink(page, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationService.getNotificationUsers(
                        pageLink,
                        isRead,
                        appUser
                ));
    }
}
