package com.cloudmore.wage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserWage {
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @NotNull
    private BigDecimal wage;
    @NotNull
    private Instant eventTime;
}
