package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.domain.Country;
import com.example.onboarding3.repositories.CountriesMongoRepo;
import com.example.onboarding3.restclients.CountriesRestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
public class CountriesServiceTest {

    @Container @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7");

    @Container @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");

    @Container
    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("docker.io/redis/redis-stack"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getFirstMappedPort());
    }

    // new addition
    @MockitoBean
    private CacheManager cacheManager;

    @MockitoBean
    CountriesRestClient restCountries;

    @MockitoBean
    CountriesMongoRepo countriesRepo;

    @Autowired
    CountriesService countriesService;

    @Test
    void findByCapital_returnsValueFromRestClient_onFirstCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(countriesRepo.findByCapital("Athens")).thenReturn(null);
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);

        CountriesCache result = countriesService.findByCapital("Athens");
        assertEquals("Greece", result.getCountryName());
        verify(restCountries, times(1)).getByCapital("Athens");
    }

    @Test
    void findByCapital_returnsValueFromMongo_onSecondCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(countriesRepo.findByCapital("Athens"))
                .thenReturn(null)     // first call
                .thenReturn(greece);        // second call
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);

        CountriesCache first  = countriesService.findByCapital("Athens");
        CountriesCache second = countriesService.findByCapital("Athens");

        assertThat(first.getCountryName()).isEqualTo("Greece");
        assertThat(second.getCountryName()).isEqualTo("Greece");

        verify(countriesRepo, times(2)).findByCapital("Athens");
        verify(restCountries, times(1)).getByCapital("Athens");
    }

    @Test
    void findByCapital_storesEntryInMongo_onFirstCall() {
        CountriesCache greece =
                new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro");
        Mockito.when(restCountries.getByCapital("Athens")).thenReturn(greece);
        Mockito.when(countriesRepo.findByCapital("Athens")).thenReturn(null);

        CountriesCache result = countriesService.findByCapital("Athens");

        verify(countriesRepo, times(1)).save(greece);
    }
}