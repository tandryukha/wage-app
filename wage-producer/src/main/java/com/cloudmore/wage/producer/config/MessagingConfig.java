package com.cloudmore.wage.producer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "messaging")
@Data
@Validated
public class MessagingConfig {
    @NotBlank
    private String userWageTopic;
    @NotNull
    private Integer partitions;
}
