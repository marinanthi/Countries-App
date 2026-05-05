package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.restclients.CountriesRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RedisCacheServiceIntTest extends BaseIntTestClass {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    RedisCacheService redisService;

    @Autowired
    CacheManager cacheManager;

    @MockitoBean
    CountriesRestClient restCountries;

    @BeforeEach
    void clearCacheBetweenTests() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void getByCapital_returnsValueFromRestClient_onFirstCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);

        CountriesCache result = redisService.getByCapital("Athens");

        assertThat(result.getCountryName()).isEqualTo("Greece");
        verify(restCountries, times(1)).getByCapital("Athens");
    }

    @Test
    void getByCapital_isServedFromRedisCache_onSecondCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);

        CountriesCache first  = redisService.getByCapital("Athens"); // hits rest client + caches in Redis
        CountriesCache second = redisService.getByCapital("Athens"); // should come from Redis

        assertThat(first.getCountryName()).isEqualTo("Greece");
        assertThat(second.getCountryName()).isEqualTo("Greece");

        verify(restCountries, times(1)).getByCapital("Athens");
    }

    @Test
    void getByCapital_storesEntryInRedisCacheNamed_country() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);

        redisService.getByCapital("Athens");
        redisService.getByCapital("Athens");

        // Verify the value really sits in the Redis-backed Spring cache.
        var cached = cacheManager.getCache("country").get("Athens", CountriesCache.class);
        assertThat(cached).isNotNull();
        assertThat(cached.getCountryName()).isEqualTo("Greece");
    }
}