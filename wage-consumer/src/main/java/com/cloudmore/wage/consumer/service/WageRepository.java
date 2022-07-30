package com.cloudmore.wage.consumer.service;

import com.cloudmore.wage.consumer.entity.Wage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WageRepository extends JpaRepository<Wage,Long> {
}
