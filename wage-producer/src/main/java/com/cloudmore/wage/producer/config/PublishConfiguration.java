package com.cloudmore.wage.producer.config;

import com.cloudmore.wage.producer.dto.UserWage;
import com.cloudmore.wage.producer.service.WagePublishService;
import com.cloudmore.wage.producer.service.WagePublishServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class PublishConfiguration {

    @Bean
    public WagePublishService wagePublishService(@Value("${messaging.user-wage-topic}") String wageTopic,
                                                 KafkaTemplate<Integer, UserWage> kafkaTemplate) {
        return new WagePublishServiceImpl(wageTopic, kafkaTemplate);
    }
}
