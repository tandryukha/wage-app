package com.cloudmore.wage.producer.topic;

public interface MessageProducer {
    void send(String topic, Object payload);
}
