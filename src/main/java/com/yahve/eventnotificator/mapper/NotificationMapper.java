package com.yahve.eventnotificator.mapper;

import com.yahve.eventnotificator.dto.NotificationDto;
import com.yahve.eventnotificator.entity.Notification;
import com.yahve.eventnotificator.entity.NotificationFieldChange;
import com.yahve.eventnotificator.event.KafkaEventMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Component
public class NotificationMapper {

  public Notification toNotification(KafkaEventMessage msg, Long subscriberId) {
    Notification notification = Notification.builder()
      .userId(subscriberId)
      .eventId(msg.getEventId())
      .createdAt(LocalDateTime.now())
      .read(false)
      .build();

    addFieldChangeIfPresent(notification, "name", msg.getName());
    addFieldChangeIfPresent(notification, "maxPlaces", msg.getMaxPlaces());
    addFieldChangeIfPresent(notification, "date", msg.getDate());
    addFieldChangeIfPresent(notification, "cost", msg.getCost());
    addFieldChangeIfPresent(notification, "duration", msg.getDuration());
    addFieldChangeIfPresent(notification, "locationId", msg.getLocationId());
    addFieldChangeIfPresent(notification, "status", msg.getStatus());

    return notification;
  }

  private <T> void addFieldChangeIfPresent(Notification notification, String fieldName, KafkaEventMessage.FieldChange<T> change) {
    if (change != null) {
      NotificationFieldChange fieldChange = NotificationFieldChange.builder()
        .fieldName(fieldName)
        .oldValue(change.getOldValue().toString())
        .newValue(change.getNewValue().toString())
        .build();

      notification.addFieldChange(fieldChange);
    }
  }

  public NotificationDto toDto(Notification notification) {
    NotificationDto dto = new NotificationDto();
    dto.setEventId(notification.getEventId());
    dto.setId(notification.getId());

    for (NotificationFieldChange change : notification.getFieldChanges()) {
      var changeDto = new NotificationDto.FieldChangeDto(change.getOldValue(), change.getNewValue());
      switch (change.getFieldName()) {
        case "name" -> dto.setName(changeDto);
        case "maxPlaces" -> dto.setMaxPlaces(changeDto);
        case "date" -> dto.setDate(changeDto);
        case "cost" -> dto.setCost(changeDto);
        case "duration" -> dto.setDuration(changeDto);
        case "locationId" -> dto.setLocationId(changeDto);
        case "status" -> dto.setStatus(changeDto);
      }
    }

    return dto;
  }
}

