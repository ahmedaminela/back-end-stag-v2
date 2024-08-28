package com.example.rh.controller;

import com.example.rh.dto.NotificationResponse;
import com.example.rh.enums.NotificationType;
import com.example.rh.mapper.NotificationMapper;
import com.example.rh.model.Notification;
import com.example.rh.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(@RequestParam Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        List<NotificationResponse> response = notifications.stream()
                .map(NotificationMapper::toNotificationResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/mark-as-read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/mark-all-as-read")
    public ResponseEntity<Void> markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<NotificationResponse> createNotification(
            @RequestParam Long userId,
            @RequestParam String message,
            @RequestParam NotificationType type) {
        Notification notification = notificationService.createNotification(userId, message, type);
        NotificationResponse response = NotificationMapper.toNotificationResponse(notification);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
