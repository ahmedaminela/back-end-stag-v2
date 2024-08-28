package com.example.rh.mapper;

import com.example.rh.dto.NotificationResponse;
import com.example.rh.model.Notification;

public class NotificationMapper {

    public static NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .recipientId(notification.getRecipient().getId())
                .message(notification.getMessage())
                .read(notification.isRead())
                .timestamp(notification.getTimestamp())
                .type(notification.getType())
                .build();
    }
}
