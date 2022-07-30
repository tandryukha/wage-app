package com.cloudmore.wage.consumer.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Wage {
    @Id
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @NotNull
    private Double wage;
    @NotNull
    private Instant eventTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wage wage = (Wage) o;

        return id.equals(wage.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
