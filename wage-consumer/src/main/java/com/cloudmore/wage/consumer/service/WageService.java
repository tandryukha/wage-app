package com.cloudmore.wage.consumer.service;

import com.cloudmore.wage.model.UserWage;

import java.util.List;

public interface WageService {
    List<UserWage> findAll();
}
