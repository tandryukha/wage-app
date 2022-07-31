package com.cloudmore.wage.consumer;


import com.cloudmore.wage.consumer.listener.WageListener;
import com.cloudmore.wage.model.UserWage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith(OutputCaptureExtension.class)
@Slf4j
@AutoConfigureMockMvc
public class WageControllerIT {

    @Container
    public static MySQLContainer<?> mySqlDB = new MySQLContainer<>("mysql:8.0.30")
            .withDatabaseName("wage-db")
            .withUsername("admin")
            .withPassword("admin");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WageListener wageListener;//no need to listen

    @Test
    public void shouldPersistWageCorrectly() throws Exception {
        Instant time = Instant.now();
        UserWage userWage = UserWage.builder()
                .name("Bill")
                .surname("Gates")
                .wage(BigDecimal.valueOf(1000000.4))
                .eventTime(time)
                .build();
        UserWage expected = UserWage.builder()
                .name("Bill")
                .surname("Gates")
                .wage(BigDecimal.valueOf(1100000.44))
                .eventTime(time)
                .build();

        mockMvc.perform(post("/wage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWage)))
                .andExpect(status().isCreated());


        List<UserWage> actual = objectMapper.readValue(mockMvc.perform(get("/wage"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEqualTo(List.of(expected));


    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySqlDB::getJdbcUrl);
        registry.add("spring.datasource.username", mySqlDB::getUsername);
        registry.add("spring.datasource.password", mySqlDB::getPassword);
    }

}
