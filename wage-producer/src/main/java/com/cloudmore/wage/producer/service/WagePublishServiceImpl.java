package com.cloudmore.wage.producer.service;

import com.cloudmore.wage.producer.dto.UserWage;
import com.cloudmore.wage.producer.topic.MessageProducer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WagePublishServiceImpl implements WagePublishService {
    private final MessageProducer messageProducer;
    private final String wageTopic;

    @Override
    public void publishWage(UserWage wage) {
        messageProducer.send(wageTopic, wage);
    }
}