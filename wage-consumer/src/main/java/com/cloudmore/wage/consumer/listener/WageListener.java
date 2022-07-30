package com.cloudmore.wage.consumer.listener;

import com.cloudmore.wage.consumer.service.WageService;
import com.cloudmore.wage.model.UserWage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WageListener {
    private final WageService wageService;

    @KafkaListener(groupId = "groups", topics = "${messaging.user-wage-topic}")
    public void receive(UserWage userWage) {
        log.info("received payload='{}'", userWage.toString());
        wageService.save(userWage);
    }

}