package com.cloudmore.wage.producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics {

    @Bean
    public NewTopic topic(MessagingConfig messagingConfig) {
        return TopicBuilder.name(messagingConfig.getUserWageTopic())
                .partitions(messagingConfig.getPartitions())
                .build();
    }

}
