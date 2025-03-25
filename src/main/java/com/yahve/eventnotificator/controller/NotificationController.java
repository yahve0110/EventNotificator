package com.yahve.eventnotificator.controller;

import com.yahve.eventnotificator.dto.MarkNotificationDto;
import com.yahve.eventnotificator.dto.NotificationDto;
import com.yahve.eventnotificator.security.JwtUserUtil;
import com.yahve.eventnotificator.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
  private final NotificationService notificationService;
  private final JwtUserUtil jwtUserUtil;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getUserNotifications() {
    Long userId = jwtUserUtil.getCurrentUserId();
    logger.info("Request for notifications with user id: {}", userId);

    List<NotificationDto> notifications = notificationService.getUserNotifications(userId);

    if (notifications.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(notifications);
  }

  @PostMapping
  public ResponseEntity<String> markAsRead(@RequestBody MarkNotificationDto request) {
    Long userId = jwtUserUtil.getCurrentUserId();
    notificationService.markAsRead(userId, request.notificationIds());
    return ResponseEntity.ok("Notifications marked as read");
  }
}
