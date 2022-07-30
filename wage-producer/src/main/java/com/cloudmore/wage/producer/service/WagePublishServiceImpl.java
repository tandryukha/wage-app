package com.cloudmore.wage.producer.service;

import com.cloudmore.wage.model.UserWage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
@Slf4j
public class WagePublishServiceImpl implements WagePublishService {
    private final String wageTopic;
    private final KafkaTemplate<Integer, UserWage> kafkaTemplate;


    @Override
    public void publishWage(UserWage wage) {
        log.info("sending payload='{}' to topic='{}'", wage, wageTopic);
        kafkaTemplate.send(wageTopic, wage);
    }
}