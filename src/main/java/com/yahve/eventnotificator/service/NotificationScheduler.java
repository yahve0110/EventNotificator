package com.yahve.eventnotificator.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

  private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
  private final NotificationService notificationService;

  @Value("${notification.cleanup.days}")
  private int cleanupDays;

  @Scheduled(cron = "${scheduler.notification-cleanup-cron}")
  @Transactional
  public void deleteOldNotifications() {
    LocalDateTime threshold = LocalDateTime.now().minusDays(cleanupDays);
    logger.info("Initiating cleanup of notifications before {}", threshold);

    int deletedCount = notificationService.deleteNotificationsBefore(threshold);

    logger.info("Deleted {} old notifications", deletedCount);
  }
}
