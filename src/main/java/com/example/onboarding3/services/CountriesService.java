package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.repositories.CountriesMongoRepo;
import com.example.onboarding3.restclients.CountriesRestClient;
import com.example.onboarding3.domain.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountriesService {

    Logger log = LoggerFactory.getLogger(CountriesService.class.getName());
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
            log.info("Country found in Mongo");
            return mongoCountry;
        } else {
            CountriesCache apiCountry = restCountries.getByCapital(capitalName);
            countriesRepo.save(apiCountry);
            log.info("Sending country from API");
            return apiCountry;
        }
    }

    @Transactional()
    public CountriesCache findByName(String countryName) {
        CountriesCache mongoCountry = countriesRepo.findByName(countryName);

        if(mongoCountry != null) {
            log.info("Country found in Mongo");
            return mongoCountry;
        } else {
            CountriesCache apiCountry = restCountries.getByName(countryName);
            countriesRepo.save(apiCountry);
            log.info("Sending country from API");
            return apiCountry;
        }
    }
}