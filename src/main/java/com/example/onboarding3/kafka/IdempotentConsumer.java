package com.example.onboarding3.kafka;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.repositories.KafkaCountriesMongoRepo;
import com.example.onboarding3.services.KafkaService;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.Duration;

@Component
public class IdempotentConsumer {

    private final KafkaCountriesMongoRepo kafkaCountriesMongoRepo;
    Logger log = LoggerFactory.getLogger(KafkaService.class.getName());
    private final KafkaConsumer<String, Country> consumer;
    private final RedisTemplate<String, String> redis;

    public IdempotentConsumer(KafkaConsumer<String, Country> consumer, RedisTemplate<String, String> redis, KafkaCountriesMongoRepo kafkaCountriesMongoRepo) {
        this.consumer = consumer;
        this.redis = redis;
        this.kafkaCountriesMongoRepo = kafkaCountriesMongoRepo;
    }

    @PostConstruct
    public void init() {
        consumer.subscribe(List.of("countries-topic-2"));
    }

//    kafka consumer
    @Transactional
    public void processRecord(ConsumerRecord<String, Country> record) {
        String idempotencyKey = String.valueOf(record.offset());
        boolean absent = tryProcess(idempotencyKey);

        if (!absent) {
            log.info("Skipping duplicate message. Offset: {}", record.offset());
        } else {
            //     every 10ms poll messages from kafka
            consumer.poll(Duration.ofMillis(10));
            log.info("Received message from Kafka. Offset: {}", record.offset());
        }
    }

    //    setting key in redis if absent
    public boolean tryProcess(String idempotencyKey) {
        Boolean wasSet = redis.opsForValue().setIfAbsent(idempotencyKey, "processed");
        return Boolean.TRUE.equals(wasSet);
    }
}