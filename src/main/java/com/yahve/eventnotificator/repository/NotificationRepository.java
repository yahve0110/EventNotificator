package com.yahve.eventnotificator.repository;

import com.yahve.eventnotificator.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findByUserIdAndReadFalse(Long userId);
  int deleteByCreatedAtBefore(LocalDateTime dateTime);
}
