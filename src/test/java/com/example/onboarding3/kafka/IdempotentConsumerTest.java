package com.example.onboarding3.kafka;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.services.BaseIntTestClass;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class IdempotentConsumerTest extends BaseIntTestClass {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    IdempotentConsumer idempotentConsumer;

    @MockitoBean
    KafkaConsumer kafkaConsumer;

    @BeforeEach
    void clearCacheBetweenTests() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void shouldProcessRecord_ifAbsentIsTrue() throws InterruptedException {
        ConsumerRecord<String, Country> record =
                new ConsumerRecord<>("countries-topic-2", 0, 1L, "key", new Country());
        idempotentConsumer.processRecord(record);

        verify(kafkaConsumer, times(1)).poll(Duration.ofMillis(10));
    }

    @Test
    void skipDuplicate_ifAbsentIsFalse() {
        ConsumerRecord<String, Country> record =
                new ConsumerRecord<>("countries-topic-2", 0, 1L, "key", new Country());
        idempotentConsumer.processRecord(record);
        idempotentConsumer.processRecord(record);

        verify(kafkaConsumer, times(1)).poll(Duration.ofMillis(10));
    }

    @Test
    void tryProcessSuccess() {
        boolean first = idempotentConsumer.tryProcess("123");
        boolean second = idempotentConsumer.tryProcess("123");

        assertTrue(first);
        assertFalse(second);
    }
}