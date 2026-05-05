package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.services.CountriesService;
import org.springframework.web.bind.annotation.*;

@RestController
public class MongoController {

    private final CountriesService countriesService;

    public MongoController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/capital/{capitalName}")
    public Country byCapital(@PathVariable String capitalName) {
        System.out.println("Searching country based on capital...");
        return countriesService.findByCapital(capitalName);
    }

    @GetMapping("/country")
    public Country byName(@RequestParam String countryName) {
        System.out.println("Searching country based on name...");
        return countriesService.findByName(countryName);
    }
}