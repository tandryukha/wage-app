package com.cloudmore.wage.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserWage {
    private String name;
    private String surname;
    private Double wage;
    private Instant eventTime;
}
