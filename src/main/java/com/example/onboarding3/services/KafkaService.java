package com.example.onboarding3.services;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.kafka.IdempotentConsumer;
import com.example.onboarding3.repositories.KafkaCountriesMongoRepo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {

    Logger log = LoggerFactory.getLogger(KafkaService.class.getName());
    private final KafkaTemplate<String, Country> kafka;
    private final KafkaCountriesMongoRepo kafkaCountriesRepo;
    private final IdempotentConsumer consumer;

    public KafkaService(KafkaTemplate<String, Country> kafka, KafkaCountriesMongoRepo kafkaCountriesRepo, IdempotentConsumer consumer) {
        this.kafka = kafka;
        this.kafkaCountriesRepo = kafkaCountriesRepo;
        this.consumer = consumer;
    }

    // producer
    public void sendCountry(KafkaCountries country) {
        CompletableFuture<SendResult<String, Country>> future = kafka.send("countries-topic", country);
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Error sending message: {}", exception.getMessage());
            } else {
                log.info("Message sent successfully. Offset: {}", result.getRecordMetadata().offset());
//                System.out.println(result.getProducerRecord().value());
            }
        });
    }

    // consumer
    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "countries-topic", groupId = "country-group")
    public void consumeCountry(KafkaCountries country) {
        kafkaCountriesRepo.save(country);
        log.info("Received message: {}", country);
    }

    public KafkaCountries getCountry(String countryName) {
        return kafkaCountriesRepo.findByName(countryName);
    }

    // producer and consumer & save country to mongo
    @Transactional(rollbackFor = Exception.class)
    public void sendCountryToKafka(KafkaCountries country) {
        SendResult<String, Country> res = kafka.send("countries-topic-2", country).join();
//        System.out.println("sent");
        log.info("Message sent successfully. Offset: {}", res.getRecordMetadata().offset());
//        Thread.sleep(5000);
        ConsumerRecord<String, Country> record
                = new ConsumerRecord<String, Country>("countries-topic-2", res.getRecordMetadata().partition(), res.getRecordMetadata().offset(), res.getProducerRecord().key(), res.getProducerRecord().value());
        consumer.processRecord(record);
//        System.out.println("consumed");
//        Thread.sleep(5000);
        kafkaCountriesRepo.save(country);
//        System.out.println("saved");
//        Thread.sleep(1000);
    }

    /*
    // testing transactional
    @Transactional
    public String forLoop(List<KafkaCountries> d) throws InterruptedException {
        for(KafkaCountries c : d) {
            System.out.println("Sending: " + c);
            sendCountryToKafka(c);
            System.out.println("Sent: " + c);
        }
        return "Done";
    }
    */
}
