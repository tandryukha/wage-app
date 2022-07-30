package com.cloudmore.wage.producer.service;

import com.cloudmore.wage.producer.dto.UserWage;

public interface WagePublishService {
    void publishWage(UserWage wage);
}
