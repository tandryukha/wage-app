package com.cloudmore.wage.consumer.config;

import com.cloudmore.wage.consumer.mapper.WageMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public WageMapper wageMapper(){
        return Mappers.getMapper(WageMapper.class);
    }
}
