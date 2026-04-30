package com.example.onboarding3.services;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.kafka.IdempotentConsumer;
import com.example.onboarding3.repositories.KafkaCountriesMongoRepo;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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

//    @Test
//    void sendsCountryToKafka() {
//        KafkaCountries country = new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "euro");
//        CompletableFuture<SendResult<String, KafkaCountries>> future = new CompletableFuture<>();
//        Mockito.when(kafkaTemplate.send("countries", country)).thenReturn();
//        kafkaService.sendCountry(country);
//
//        verify(kafkaTemplate, times(1)).send("country", country);
//    }

    @Test
    void consumesCountryAndSavesToDatabase() throws InterruptedException {
        KafkaCountries country = new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "euro");

        kafkaService.consumeCountry(country);

        verify(kafkaCountriesRepo, times(1)).save(country);
    }

    @Test
    void sendsConsumesAndSavesCountry() throws InterruptedException {
        KafkaCountries greece = new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "euro");
//        CompletableFuture<SendResult<String, KafkaCountries>> future = CompletableFuture.supplyAsync(() -> new SendResult<String, KafkaCountries>());
//        Mockito.when(kafkaTemplate.send("country", greece)).thenReturn();
        kafkaService.sendCountryToKafka(greece);

//        verify(kafkaTemplate, times(1)).send("country", greece);
//        verify(kafkaCountriesRepo, times(1)).save(greece);
    }
}