package com.cloudmore.wage.producer.config;

import com.cloudmore.wage.model.UserWage;
import com.cloudmore.wage.producer.service.WagePublishService;
import com.cloudmore.wage.producer.service.WagePublishServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class PublishConfiguration {

    @Bean
    public WagePublishService wagePublishService(MessagingConfig messagingConfig,
                                                 KafkaTemplate<Integer, UserWage> kafkaTemplate) {
        return new WagePublishServiceImpl(messagingConfig.getUserWageTopic(), kafkaTemplate);
    }
}
