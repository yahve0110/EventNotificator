package com.yahve.eventnotificator.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_field_changes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFieldChange {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notification_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Notification notification;

  @Column(name = "field_name", nullable = false)
  private String fieldName;

  @Column(name = "old_value")
  private String oldValue;

  @Column(name = "new_value")
  private String newValue;
}
