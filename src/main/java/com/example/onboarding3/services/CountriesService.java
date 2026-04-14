package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.repositories.CountriesMongoRepo;
import com.example.onboarding3.restclients.CountriesRestClient;
import com.example.onboarding3.domain.Country;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountriesService {

    private final CountriesRestClient restCountries;
    private final CountriesMongoRepo countriesRepo;

    public CountriesService(CountriesRestClient restCountries, CountriesMongoRepo countriesRepo) {
        this.restCountries = restCountries;
        this.countriesRepo = countriesRepo;
    }

    @Transactional
    public CountriesCache findByCapital(String capitalName) {
        CountriesCache mongoCountry = countriesRepo.findByCapital(capitalName);

        if(mongoCountry != null) {
            System.out.println("Country found in Mongo");
            return mongoCountry;
        }
        CountriesCache apiCountry = restCountries.getByCapital(capitalName);
        countriesRepo.save(apiCountry);
        System.out.println("Sending from API");
        return apiCountry;
    }

    @Transactional
    public Country findByName(String countryName) {
        CountriesCache mongoCountry = countriesRepo.findByName(countryName);

        if(mongoCountry != null) {
            System.out.println("Country found in Mongo");
            return mongoCountry;
        }
        CountriesCache apiCountry = restCountries.getByName(countryName);
        System.out.println("Sending from API");
        countriesRepo.save(apiCountry);
        return apiCountry;
    }
}