package com.example.onboarding3.repositories;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.domain.Country;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesMongoRepo extends MongoRepository<CountriesCache,String> {
    @Query("{'capital': ?0}")
    CountriesCache findByCapital(String capital);

    @Query("{'countryName': ?0}")
    CountriesCache findByName(String name);
}