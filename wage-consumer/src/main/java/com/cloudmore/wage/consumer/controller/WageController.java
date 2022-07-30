package com.cloudmore.wage.consumer.controller;

import com.cloudmore.wage.consumer.service.WageService;
import com.cloudmore.wage.model.UserWage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Exists to make application more testable
 */
@RestController
@RequestMapping("/wage")
@RequiredArgsConstructor
public class WageController {

    private final WageService wageService;

    @GetMapping
    public List<UserWage> getUserWages() {
        return wageService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserWage(@RequestBody UserWage userWage) {
        wageService.save(userWage);
    }
}
