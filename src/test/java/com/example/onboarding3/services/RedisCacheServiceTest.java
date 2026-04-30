package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.restclients.CountriesRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
class RedisCacheServiceTest {
//    DockerImageName myMongoImage = DockerImageName.parse("docker.io/mongodb/mongodb-community-server").asCompatibleSubstituteFor("apache/kafka");

    @Container @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7");

//    DockerImageName myKafkaImage = DockerImageName.parse("docker.io/confluentinc/cp-kafka").asCompatibleSubstituteFor("apache/kafka");

    @Container @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");

    @Container
    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("docker.io/redis/redis-stack"))
            .withExposedPorts(6379);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort());
    }

//    @BeforeEach
//    public void setUp() {
//        String address = redis.getHost();
//        Integer port = redis.getFirstMappedPort();
//
//        System.setProperty("spring.redis.host", address);
//        System.setProperty("spring.redis.port", port.toString());
//    }

    @Autowired
    RedisCacheService redisService;

    @Autowired
    CacheManager cacheManager;

    @MockitoBean
    CountriesRestClient restCountries;

    @BeforeEach
    void clearCacheBetweenTests() {
//        cacheManager.getCacheNames()
//                .forEach(name -> cacheManager.getCache(name).clear());
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

        // Verify the value really sits in the Redis-backed Spring cache.
        var cached = cacheManager.getCache("country").get("Athens", CountriesCache.class);
        assertThat(cached).isNotNull();
        assertThat(cached.getCountryName()).isEqualTo("Greece");
    }
}