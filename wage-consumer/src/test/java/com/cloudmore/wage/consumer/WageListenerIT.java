package com.cloudmore.wage.consumer;


import com.cloudmore.wage.model.UserWage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Slf4j
@AutoConfigureMockMvc
/**
 * Testing Kafka + REST + JPA levels
 */
public class WageListenerIT {

    public static final Instant EVENT_DATE = Instant.now();
    @Container
    public static MySQLContainer<?> mySqlDB = new MySQLContainer<>("mysql:8.0.30")
            .withDatabaseName("wage-db")
            .withUsername("admin")
            .withPassword("admin");

    static KafkaContainer kafka;
    static {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        kafka.start();
    }

    @Autowired
    private KafkaAdmin admin;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${messaging.user-wage-topic}")
    private String topicName;

    @BeforeEach
    void setUp() {
        createTopic();
    }

    @Test
    public void shouldReceiveWage() throws Exception {
        getProducer().send(new ProducerRecord<>(topicName, getUserWage(1000000.4, EVENT_DATE))).get();

        await().atMost(20, TimeUnit.SECONDS).until(() -> {
            List<UserWage> actual = objectMapper.readValue(mockMvc.perform(get("/wage"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(), new TypeReference<>() {
            });
            if (actual.isEmpty()) return false;
            assertThat(actual).isEqualTo(List.of(getUserWage(1100000.44, EVENT_DATE)));
            return true;
        });

    }

    private void createTopic() {
        AdminClient client = AdminClient.create(admin.getConfigurationProperties());
        client.createTopics(Collections.singletonList(TopicBuilder.name(topicName).build()));
    }

    private static UserWage getUserWage(double val, Instant now) {
        return UserWage.builder()
                .name("Bill")
                .surname("Gates")
                .wage(BigDecimal.valueOf(val))
                .eventTime(now)
                .build();
    }

    @NotNull
    private static KafkaProducer<Integer, UserWage> getProducer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return (KafkaProducer<Integer, UserWage>) new KafkaProducer(props);
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);

        registry.add("spring.datasource.url", mySqlDB::getJdbcUrl);
        registry.add("spring.datasource.username", mySqlDB::getUsername);
        registry.add("spring.datasource.password", mySqlDB::getPassword);
    }

}
