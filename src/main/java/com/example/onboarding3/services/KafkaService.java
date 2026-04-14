package com.example.onboarding3.services;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.repositories.KafkaCountriesMongoRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaService {

    private final KafkaTemplate<String, Country> KafkaTemplate;
    private final KafkaCountriesMongoRepo kafkaCountriesRepo;

    public KafkaService(KafkaTemplate<String, Country> KafkaTemplate, KafkaCountriesMongoRepo kafkaCountriesRepo) {
        this.KafkaTemplate = KafkaTemplate;
        this.kafkaCountriesRepo = kafkaCountriesRepo;
    }

    @Transactional
    public void sendCountry(KafkaCountries country) {
        KafkaTemplate.send("countries-topic", country);
        consumeCountry(country);
    }

    @KafkaListener(topics = "countries-topic", groupId = "country-group")
    public void consumeCountry(KafkaCountries country) {
        kafkaCountriesRepo.save(country);
    }

    public KafkaCountries getCountry(String countryName) {
        return kafkaCountriesRepo.findByName(countryName);
    }
}
