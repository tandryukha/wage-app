package com.cloudmore.wage.producer;

import com.cloudmore.wage.model.UserWage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Slf4j
@AutoConfigureMockMvc
public class KafkaProducerApplicationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    static final KafkaContainer kafka;
    @Value("${messaging.user-wage-topic}")
    private String topicName;


    static {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        kafka.start();
    }

    @Autowired
    private KafkaAdmin admin;

    @Test
    public void shouldCreateWageTopicAtStartup() throws Exception {
        AdminClient client = AdminClient.create(admin.getConfigurationProperties());
        Collection<TopicListing> topicList = client.listTopics().listings().get();
        assertNotNull(topicList);
        assertEquals(topicList.stream().map(TopicListing::name).toList(), List.of(topicName));
    }

    @Test
    public void shouldPublishWage() throws Exception {
        UserWage userWage = getUserWage();
        KafkaConsumer<Integer, UserWage> testConsumer = createTestConsumer(topicName);

        mockMvc.perform(post("/wage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWage)))
                .andExpect(status().isCreated());

        await().atMost(20, TimeUnit.SECONDS).until(() -> {
            ConsumerRecords<Integer, UserWage> records = testConsumer.poll(Duration.ofMillis(100));
            if (records.isEmpty()) return false;
            records.forEach(r -> log.info(r.topic() + " *** " + r.key() + " *** " + r.value()));
            assertThat(records.count()).isEqualTo(1);
            assertThat(records.iterator().next().value()).isEqualTo(userWage);
            return true;
        });

    }

    private static UserWage getUserWage() {
        return UserWage.builder()
                .name("Bill")
                .surname("Gates")
                .wage(BigDecimal.valueOf(1000000.4))
                .eventTime(Instant.now())
                .build();
    }

    private static KafkaConsumer<Integer, UserWage> createTestConsumer(String topicName) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "UserWage:com.cloudmore.wage.model.UserWage");
        KafkaConsumer<Integer, UserWage> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(topicName));
        return consumer;
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.properties.bootstrap.servers", kafka::getBootstrapServers);
    }

}
