package com.example.onboarding3.services;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.kafka.IdempotentConsumer;
import com.example.onboarding3.repositories.KafkaCountriesMongoRepo;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
//@WebMvcTest(KafkaService.class)
@Testcontainers
class KafkaServiceTest {
    @Container @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7");

    @Container @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");

    @Container
    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("docker.io/redis/redis-stack"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort());
    }

    @Autowired
    KafkaService kafkaService;

    @MockitoBean
    IdempotentConsumer consumer;

    @MockitoBean
    KafkaCountriesMongoRepo kafkaCountriesRepo;

    @MockitoBean
    private KafkaTemplate<String, Country> kafkaTemplate;

    @Test
    void sendCountryTest() {
        when(kafkaTemplate.send(anyString(), any()))
                .thenReturn(new CompletableFuture<>());
        kafkaService.sendCountry(new KafkaCountries());
    }

    @Test
    void consumesCountryAndSavesToDatabase() throws InterruptedException {
        KafkaCountries country = new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "euro");

        kafkaService.consumeCountry(country);

        verify(kafkaCountriesRepo, times(1)).save(country);
    }

    @Test
    void sendsConsumesAndSavesCountry() throws InterruptedException {
        KafkaCountries greece = new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        ProducerRecord<String, Country> producerRecord =
                new ProducerRecord<>("countries-topic-2", "key", new Country());

        RecordMetadata metadata = new RecordMetadata(
                new TopicPartition("countries-topic-2", 0),
                0,   // baseOffset
                123, // offset
                System.currentTimeMillis(),
               0,
                0
        );

        SendResult<String, Country> sendResult =
                new SendResult<>(producerRecord, metadata);

        when(kafkaTemplate.send(anyString(), any()))
                .thenReturn(CompletableFuture.completedFuture(sendResult));

        kafkaService.sendCountryToKafka(greece);
        Thread.sleep(2000);

        verify(kafkaCountriesRepo, times(1)).save(greece);
        verify(kafkaTemplate, times(1)).send(anyString(), any());
        verify(consumer, times(1)).processRecord(any());
    }
}