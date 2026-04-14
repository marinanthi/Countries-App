package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.services.CountriesService;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final CountriesService countriesService;

    public Controller(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/capital/{capitalName}")
    public Country byCapital(@PathVariable String capitalName) {
        System.out.println("Searching country based on capital...");
        return countriesService.findByCapital(capitalName);
    }

    @GetMapping("/country")
    public Country byName(@RequestParam String country_name) {
        System.out.println("Searching country based on name...");
        return countriesService.findByName(country_name);
    }
}