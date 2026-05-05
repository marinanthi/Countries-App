package com.example.onboarding3.services;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.kafka.IdempotentConsumer;
import com.example.onboarding3.repositories.KafkaCountriesMongoRepo;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class KafkaServiceIntTest extends BaseIntTestClass {

    @Autowired
    KafkaService kafkaService;

    @MockitoBean
    IdempotentConsumer consumer;

    @MockitoBean
    KafkaCountriesMongoRepo kafkaCountriesRepo;

    @MockitoBean
    private KafkaTemplate<String, Country> kafkaTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void clearMongo() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void sendCountryTest() {
        when(kafkaTemplate.send(anyString(), any())).thenReturn(new CompletableFuture<>());
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