package com.minhvu.notification.service;


import com.minhvu.notification.dto.model.AppUserDto;
import com.minhvu.notification.dto.model.NotificationUserResponseDto;
import com.minhvu.notification.dto.page.PageData;
import com.minhvu.notification.dto.page.PageLink;

public interface NotificationService {
    PageData<NotificationUserResponseDto> getNotificationUsers(PageLink pageLink, Boolean isRead, AppUserDto appUserDto);
}
