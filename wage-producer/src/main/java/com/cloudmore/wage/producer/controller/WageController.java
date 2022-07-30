package com.cloudmore.wage.producer.controller;

import com.cloudmore.wage.model.UserWage;
import com.cloudmore.wage.producer.service.WagePublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/wage")
@RequiredArgsConstructor
public class WageController {

    private final WagePublishService wagePublishService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void publishWage(@RequestBody UserWage wage) {
        wagePublishService.publishWage(wage);
    }

}
