package com.yahve.eventnotificator.service;

import com.yahve.eventnotificator.dto.NotificationDto;
import com.yahve.eventnotificator.entity.Notification;
import com.yahve.eventnotificator.event.KafkaEventMessage;
import com.yahve.eventnotificator.exception.ResourceNotFoundException;
import com.yahve.eventnotificator.mapper.NotificationMapper;
import com.yahve.eventnotificator.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Value("${notification.cleanup.days}")
  private int cleanupDays;

  @Transactional
  public void createNotifications(KafkaEventMessage message) {
    for (Long subscriberId : message.getSubscribers()) {

      logger.info("Creating notification for userId={} and eventId={}", subscriberId, message.getEventId());

      Notification notification = notificationMapper.toNotification(message, subscriberId);

      logger.debug("Mapped notification: {}", notification);

      notificationRepository.save(notification);
    }
  }

  public List<NotificationDto> getUserNotifications(Long userId) {

    List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);
    return notifications.stream()
      .map(notificationMapper::toDto)
      .toList();
  }

  @Transactional
  public void markAsRead(Long userId, List<Long> notificationIds) {
    List<Notification> notifications = notificationRepository.findAllById(notificationIds);

    List<Notification> filtered = notifications.stream()
      .filter(n -> Objects.equals(n.getUserId(), userId))
      .toList();

    if (filtered.isEmpty()) {
      throw new ResourceNotFoundException("No notifications found for the given IDs and user");
    }

    filtered.forEach(n -> n.setRead(true));
    notificationRepository.saveAll(filtered);
  }


  @Scheduled(cron = "${scheduler.notification-cleanup-cron}")
  @Transactional
  public void deleteOldNotifications() {

    LocalDateTime threshold = LocalDateTime.now().minusDays(cleanupDays);

    logger.info("Deleting notifications created before {}", threshold);

    int deletedCount = notificationRepository.deleteByCreatedAtBefore(threshold);

    logger.info("Deleted {} old notifications", deletedCount);
  }

}
