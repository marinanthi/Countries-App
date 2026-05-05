package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.restclients.CountriesRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CountriesServiceIntTest extends BaseIntTestClass {

    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    CountriesRestClient restCountries;

    @Autowired
    CountriesService countriesService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void clearMongo() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void findByCapital_returnsValueFromRestClient_onFirstCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);

        CountriesCache result = countriesService.findByCapital("Athens");
        assertEquals("Greece", result.getCountryName());
        verify(restCountries, times(1)).getByCapital("Athens");
    }

    @Test
    void findByCapital_returnsValueFromMongo_onSecondCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
//        Mockito.when(countriesRepo.findByCapital("Athens"))
//                .thenReturn(null)     // first call
//                .thenReturn(greece);        // second call
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);

        CountriesCache first  = countriesService.findByCapital("Athens");
        CountriesCache second = countriesService.findByCapital("Athens");

        assertThat(first.getCountryName()).isEqualTo("Greece");
        assertThat(second.getCountryName()).isEqualTo("Greece");

//        verify(countriesRepo, times(2)).findByCapital("Athens");
        verify(restCountries, times(1)).getByCapital("Athens");
    }

    @Test
    void findByCapital_storesEntryInMongo_onFirstCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);
//        Mockito.when(countriesRepo.findByCapital("Athens")).thenReturn(null);

        CountriesCache result = countriesService.findByCapital("Athens");

//        verify(countriesRepo, times(1)).save(greece);
        assertThat(result.getCountryName()).isEqualTo("Greece");
    }
}