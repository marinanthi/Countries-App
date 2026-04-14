package com.example.onboarding3.repositories;

import com.example.onboarding3.domain.KafkaCountries;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaCountriesMongoRepo extends MongoRepository<KafkaCountries,String> {
    @Query("{'countryName': ?0}")
    KafkaCountries findByName(String name);
}
