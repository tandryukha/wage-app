package com.cloudmore.wage.producer.service;

import com.cloudmore.wage.model.UserWage;

public interface WagePublishService {
    void publishWage(UserWage wage);
}
