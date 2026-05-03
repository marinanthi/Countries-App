package com.example.onboarding3.kafka;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.domain.KafkaCountries;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@Testcontainers
@SpringBootTest
class IdempotentConsumerTest {
//
//    @Container @ServiceConnection
//    static MongoDBContainer mongo =
//            new MongoDBContainer("mongo:7")
//                    .waitingFor(
//                            Wait.forLogMessage(".*Waiting for connections.*", 1)
//                    );
//
//    @Container @ServiceConnection
//    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");
//
//    @Container
//    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("docker.io/redis/redis-stack"))
//            .withExposedPorts(6379);
//
//    @DynamicPropertySource
//    static void redisProps(DynamicPropertyRegistry registry) {
//        registry.add("spring.data.redis.host", redis::getHost);
//        registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort());
//        System.out.println("DynamicPropertySource CALLED 2");
//    }
//
//    @Autowired
//    private RedisTemplate<Object, Object> redisTemplate;
//
//    @Autowired
//    IdempotentConsumer idempotentConsumer;
//
//    @MockitoBean
//    KafkaConsumer kafkaConsumer;
//
//    @BeforeEach
//    void clearCacheBetweenTests() {
//        redisTemplate.getConnectionFactory().getConnection().flushAll();
//    }
//
//    @Test
//    void shouldProcessRecord_ifAbsentIsTrue() throws InterruptedException {
//        ConsumerRecord<String, Country> record =
//                new ConsumerRecord<>("countries-topic-2", 0, 1L, "key", new Country());
//        idempotentConsumer.processRecord(record);
//
//        verify(kafkaConsumer, times(1)).poll(Duration.ofMillis(10));
//    }
//
//    @Test
//    void skipDuplicate_ifAbsentIsFalse() {
//        ConsumerRecord<String, Country> record =
//                new ConsumerRecord<>("countries-topic-2", 0, 1L, "key", new Country());
//        idempotentConsumer.processRecord(record);
//        idempotentConsumer.processRecord(record);
//
//        verify(kafkaConsumer, times(1)).poll(Duration.ofMillis(10));
//    }
//
//    @Test
//    void tryProcessSuccess() {
//        boolean first = idempotentConsumer.tryProcess("123");
//        boolean second = idempotentConsumer.tryProcess("123");
//
//        assertTrue(first);
//        assertFalse(second);
//    }
}