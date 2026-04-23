package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.restclients.CountriesRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {

    private final CountriesRestClient restCountries;
    private final CacheManager cacheManager;

    public RedisCacheService(CountriesRestClient restCountries, CacheManager cacheManager) {
        this.restCountries = restCountries;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "country", key = "#capital")
    public CountriesCache getByCapital(String capital) {
        return restCountries.getByCapital(capital);
    }

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.MINUTES)
    void redisCleaner() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
}