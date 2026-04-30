package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.Country;
import com.example.onboarding3.services.RedisCacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/redis")
public class RedisCacheController {

    private final RedisCacheService countriesService;

    public RedisCacheController(RedisCacheService countriesService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/findByCapital/{capitalName}")
    public Country byCapital(@PathVariable String capitalName) {
        return countriesService.getByCapital(capitalName);
    }
}