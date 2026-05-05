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

    //    project endpoint 3
    @PostMapping(path ="/kafka", consumes = "application/json")
    public void sendToKafka(@Valid @RequestBody KafkaCountries country) {
        kafkaService.sendCountry(country);
    }

    //    project endpoint 4
    @GetMapping( "/kafka/{countryName}")
    public List<KafkaCountries> getKafkaCountry(@PathVariable String countryName) {
        List<KafkaCountries> countries = new ArrayList<>();
        KafkaCountries ans = kafkaService.getCountry(countryName);
        if(ans != null) {
            countries.add(ans);
        }
        return countries;
    }

    @PostMapping(path ="/kafka2", consumes = "application/json")
    public void produceAndConsume(@Valid @RequestBody KafkaCountries country) {
        kafkaService.sendCountryToKafka(country);
    }

    /*
    //    endpoints to check idempotency and exactly once
    @GetMapping("/c")
    public String sth() throws InterruptedException {
        List<KafkaCountries> d = Arrays.asList(
                new KafkaCountries("Greece", "Athens", "Europe", "Greek", "Euro", "2"),
                new KafkaCountries("France", "Paris", "Europe", "French", "Euro", "67"),
                new KafkaCountries("Germany", "Berlin", "Europe", "German", "Euro", "83"),
                new KafkaCountries("Italy", "Rome", "Europe", "Italian", "Euro", "60"),
                new KafkaCountries("Spain", "Madrid", "Europe", "Spanish", "Euro", "47"),
                new KafkaCountries("Portugal", "Lisbon", "Europe", "Portuguese", "Euro", "10"),
                new KafkaCountries("Netherlands", "Amsterdam", "Europe", "Dutch", "Euro", "17"),
                new KafkaCountries("Belgium", "Brussels", "Europe", "Dutch/French", "Euro", "11"),
                new KafkaCountries("Sweden", "Stockholm", "Europe", "Swedish", "Krona", "10"),
                new KafkaCountries("Norway", "Oslo", "Europe", "Norwegian", "Krone", "5"),
                new KafkaCountries("Denmark", "Copenhagen", "Europe", "Danish", "Krone", "6"),
                new KafkaCountries("Finland", "Helsinki", "Europe", "Finnish", "Euro", "5"),
                new KafkaCountries("Poland", "Warsaw", "Europe", "Polish", "Zloty", "38"),
                new KafkaCountries("Austria", "Vienna", "Europe", "German", "Euro", "9"),
                new KafkaCountries("Switzerland", "Bern", "Europe", "German/French", "Franc", "8"),
                new KafkaCountries("Czech Republic", "Prague", "Europe", "Czech", "Koruna", "10"),
                new KafkaCountries("Hungary", "Budapest", "Europe", "Hungarian", "Forint", "9"),
                new KafkaCountries("Ireland", "Dublin", "Europe", "English", "Euro", "5"),
                new KafkaCountries("Romania", "Bucharest", "Europe", "Romanian", "Leu", "19")
        );

        return kafkaService.forLoop(d);
    }

    @GetMapping("/c2")
    public void sth2() throws InterruptedException {
        List<KafkaCountries> d = Arrays.asList(
                new KafkaCountries("Greece", "Athens", "Europe", "Greek", "Euro", "2"),
                new KafkaCountries("France", "Paris", "Europe", "French", "Euro", "67"),
                new KafkaCountries("Germany", "Berlin", "Europe", "German", "Euro", "83"),
                new KafkaCountries("Italy", "Rome", "Europe", "Italian", "Euro", "60"),
                new KafkaCountries("Spain", "Madrid", "Europe", "Spanish", "Euro", "47"),
                new KafkaCountries("Portugal", "Lisbon", "Europe", "Portuguese", "Euro", "10"),
                new KafkaCountries("Netherlands", "Amsterdam", "Europe", "Dutch", "Euro", "17"),
                new KafkaCountries("Belgium", "Brussels", "Europe", "Dutch/French", "Euro", "11"),
                new KafkaCountries("Sweden", "Stockholm", "Europe", "Swedish", "Krona", "10"),
                new KafkaCountries("Norway", "Oslo", "Europe", "Norwegian", "Krone", "5"),
                new KafkaCountries("Denmark", "Copenhagen", "Europe", "Danish", "Krone", "6"),
                new KafkaCountries("Finland", "Helsinki", "Europe", "Finnish", "Euro", "5"),
                new KafkaCountries("Poland", "Warsaw", "Europe", "Polish", "Zloty", "38"),
                new KafkaCountries("Austria", "Vienna", "Europe", "German", "Euro", "9"),
                new KafkaCountries("Switzerland", "Bern", "Europe", "German/French", "Franc", "8"),
                new KafkaCountries("Czech Republic", "Prague", "Europe", "Czech", "Koruna", "10"),
                new KafkaCountries("Hungary", "Budapest", "Europe", "Hungarian", "Forint", "9"),
                new KafkaCountries("Ireland", "Dublin", "Europe", "English", "Euro", "5"),
                new KafkaCountries("Romania", "Bucharest", "Europe", "Romanian", "Leu", "19")
        );

        for(KafkaCountries c : d) {
            System.out.println("Sending: " + c);
            kafkaService.sendCountry(c);
            Thread.sleep(1000);
            System.out.println("Sent: " + c);
        }
        System.out.println("Done");
    }
    */

}