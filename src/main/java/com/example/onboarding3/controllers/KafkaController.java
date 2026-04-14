package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.services.KafkaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.ArrayList;

@RestController
public class KafkaController {

    private final KafkaService kafkaService;

    public KafkaController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping(path ="/kafka", consumes = "application/json")
    public void sendToKafka(@Valid @RequestBody KafkaCountries country) {
        kafkaService.sendCountry(country);
    }

    @GetMapping( "/kafka/{capitalName}")
    public List<KafkaCountries> getKafkaCountry(@PathVariable String capitalName) {
        List<KafkaCountries> countries = new ArrayList<>();
        KafkaCountries ans = kafkaService.getCountry(capitalName);
        if(ans != null) {
            countries.add(ans);
        }
        return countries;
    }
}