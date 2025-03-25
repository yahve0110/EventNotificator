package com.yahve.eventnotificator.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "event_id", nullable = false)
  private Long eventId;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "is_read", nullable = false)
  private boolean read;

  @Builder.Default
  @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NotificationFieldChange> fieldChanges = new ArrayList<>();

  public void addFieldChange(NotificationFieldChange change) {
    if (change == null) return;

    change.setNotification(this);
    this.fieldChanges.add(change);
  }
}

