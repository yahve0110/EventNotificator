package com.yahve.eventnotificator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.yahve.eventnotificator.event.KafkaEventMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public ConsumerFactory<Long, KafkaEventMessage> consumerFactory() {
    Map<String, Object> configProperties = new HashMap<>();
    configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);

    var factory = new DefaultKafkaConsumerFactory<Long, KafkaEventMessage>(configProperties);
    factory.setValueDeserializer(new JsonDeserializer<>(KafkaEventMessage.class, false));
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<Long, KafkaEventMessage> containerFactory(
    ConsumerFactory<Long, KafkaEventMessage> consumerFactory
  ) {
    var factory = new ConcurrentKafkaListenerContainerFactory<Long, KafkaEventMessage>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }
}

