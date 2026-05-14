package com.bulkbuddy.controller;

import com.bulkbuddy.dto.mapper.DtoMapper;
import com.bulkbuddy.dto.response.NotificationResponse;
import com.bulkbuddy.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final DtoMapper dtoMapper;

    private Long getCurrentUserId(Authentication auth) {
        return (Long) auth.getCredentials();
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAll(Authentication auth) {
        return ResponseEntity.ok(dtoMapper.toNotificationResponseList(
                notificationService.getNotificationsForUser(getCurrentUserId(auth))));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread(Authentication auth) {
        return ResponseEntity.ok(dtoMapper.toNotificationResponseList(
                notificationService.getUnreadNotifications(getCurrentUserId(auth))));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count(Authentication auth) {
        return ResponseEntity.ok(Map.of("unreadCount",
                notificationService.countUnreadNotifications(getCurrentUserId(auth))));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable Long id) {
        return ResponseEntity.ok(dtoMapper.toNotificationResponse(notificationService.markAsRead(id)));
    }

    @PostMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllRead(Authentication auth) {
        notificationService.markAllAsRead(getCurrentUserId(auth));
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }
}
