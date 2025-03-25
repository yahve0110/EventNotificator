package com.yahve.eventnotificator.event;

import com.yahve.eventnotificator.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventKafkaListener {

  private static final Logger logger = LoggerFactory.getLogger(EventKafkaListener.class);
  private final NotificationService notificationService;

  @KafkaListener(topics = "${kafka.topic.event-updates}", containerFactory = "containerFactory")
  public void listenEvents(ConsumerRecord<Long, KafkaEventMessage> eventRecord) {

    logger.info("GET EVENT: EVENT={}", eventRecord.value());

    notificationService.createNotifications(eventRecord.value());
  }
}