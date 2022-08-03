package com.cloudmore.wage.consumer.listener;

import com.cloudmore.wage.consumer.service.WageService;
import com.cloudmore.wage.model.UserWage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WageListener {
    private final WageService wageService;

    @KafkaListener(groupId = "${messaging.consumer-group-name}", topics = "${messaging.user-wage-topic}")
    public void receive(@Payload UserWage userWage, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("eceived payload='{}' from partition={}", userWage, partition);
        wageService.save(userWage);
    }

}