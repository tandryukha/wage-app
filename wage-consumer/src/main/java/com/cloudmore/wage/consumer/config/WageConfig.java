package com.cloudmore.wage.consumer.config;

import com.cloudmore.wage.consumer.mapper.WageMapper;
import com.cloudmore.wage.consumer.service.WageRepository;
import com.cloudmore.wage.consumer.service.WageService;
import com.cloudmore.wage.consumer.service.WageServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class WageConfig {
    @Bean
    public WageService wageService(WageRepository wageRepository, WageMapper wageMapper, @Value("${wage.tax-rate}") BigDecimal taxRate) {
        return new WageServiceImpl(wageRepository, wageMapper, taxRate);
    }
}
