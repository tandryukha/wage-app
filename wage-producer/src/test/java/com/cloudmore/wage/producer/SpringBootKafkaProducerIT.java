package com.cloudmore.wage.producer;

import com.cloudmore.wage.producer.controller.WageController;
import com.cloudmore.wage.producer.dto.UserWage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.assertj.core.api.Assertions;
import org.jose4j.jwk.Use;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Slf4j
public class SpringBootKafkaProducerIT {
    static KafkaContainer kafka;

    static {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
        kafka.start();
    }

    @Autowired
    private WageController wageController;

    @Autowired
    private KafkaAdmin admin;


    @Test
    public void testCreationOfTopicAtStartup() throws IOException, InterruptedException, ExecutionException {
        AdminClient client = AdminClient.create(admin.getConfigurationProperties());
        Collection<TopicListing> topicList = client.listTopics().listings().get();
        assertNotNull(topicList);
        assertEquals(topicList.stream().map(l -> l.name()).collect(Collectors.toList()), Arrays.asList("create-employee-events", "springboot-topic"));
    }


    @Test
    public void shouldPublishWage() throws IOException, InterruptedException, ExecutionException {
        // first create the create-employee-events topic
        String topicName = "create-employee-events";
        NewTopic topic1 = TopicBuilder.name(topicName).build();

        AdminClient client = AdminClient.create(admin.getConfigurationProperties());
        client.createTopics(Collections.singletonList(topic1));

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "Employee:dev.fullstackcode.kafka.producer.dto.Employee");
        KafkaConsumer<Integer, UserWage> consumer = new KafkaConsumer<>(props);


        consumer.subscribe(Collections.singletonList(topicName));

        UserWage userWage = UserWage.builder()
                .name("Bill")
                .surname("Gates")
                .wage(1000000.4)
                .eventTime(Instant.now())
                .build();
        wageController.publishWage(userWage);

        Collection<TopicListing> topicList = client.listTopics().listings().get();
        assertEquals(topicList.size(), 2);
        List<String> topicNameList = topicList.stream().map(l -> l.name()).collect(Collectors.toList());
        List<String> expectedTopicNameList = Arrays.asList("springboot-topic", "create-employee-events");
        assertTrue(topicNameList.containsAll(expectedTopicNameList) && expectedTopicNameList.containsAll(topicNameList));

        await().atMost(20, TimeUnit.SECONDS).until(() -> {
            ConsumerRecords<Integer, UserWage> records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                return false;
            }
            records.forEach(r -> System.out.println(r.topic() + " *** " + r.key() + " *** " + r.value()));
            assertThat(records.count()).isEqualTo(1);
            assertThat(records.iterator().next().value()).isEqualTo(userWage);
            return true;
        });

    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.properties.bootstrap.servers", kafka::getBootstrapServers);


    }

}
