package com.cloudmore.wage.consumer.controller;

import com.cloudmore.wage.consumer.service.WageService;
import com.cloudmore.wage.model.UserWage;
import org.apache.kafka.common.message.AlterReplicaLogDirsRequestData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Exists to make application more testable
 */
@RestController("/wage")
public class WageController {

    private WageService wageService;

    @GetMapping
    public List<UserWage> getUserWages(){
        return wageService.findAll();
    }
}
