package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.dto.model.NotificationDto;
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
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/notification")
@AllArgsConstructor
public class NotificationController extends BaseController {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/read/{id}")
    @Operation(summary = "Mark Notification as Read")
    public ResponseEntity<String> readNotification(@PathVariable UUID id) {
        AppUser appUser = getCurrentUser();
        notificationService.readNotificationUser(id, appUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Notification marked as read");
    }

    @Operation(summary = "Get All Notifications for a user")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<PageData<NotificationUserResponseDto>> getNotificationUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "false") Boolean isRead) {
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

    @MessageMapping("/notification")
    public void recMessage(@Payload NotificationDto notification) {
        notificationService.saveNotification(notification);
    }
}
