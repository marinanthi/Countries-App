package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.repositories.KafkaCountriesMongoRepo;
import com.example.onboarding3.services.CountriesService;
import com.example.onboarding3.services.KafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
class KafkaControllerTest {
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

    @MockitoBean
    KafkaService kafkaService;

    @Autowired
    KafkaCountriesMongoRepo repo;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CountriesService countriesService;

    ObjectMapper objectMapper = new ObjectMapper();

    //  POST /kafka
    @Test
    void shouldSendCountryToKafka() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital("Athens");
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvc.perform(post("/kafka")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isOk());

        Mockito.verify(kafkaService, times(1))
                .sendCountry(any(KafkaCountries.class));
    }

    @Test
    void shouldNotSendCountryToKafka() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital(null);
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvc.perform(post("/kafka")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isBadRequest());
    }

    //  GET /kafka/{countryName}
    @Test
    void shouldReturnCountry_fromDatabase() throws Exception {
        Mockito.when(kafkaService.getCountry("Greece")).thenReturn(new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvc.perform(get("/kafka/Greece"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].countryName").isNotEmpty(),
                        jsonPath("$[0].countryCode").value("GR")
                );
    }

    // POST /kafka2
    @Test
    void shouldSendCountryToKafka2() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital("Athens");
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvc.perform(post("/kafka")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isOk());

        Mockito.verify(kafkaService, times(1))
                .sendCountry(any(KafkaCountries.class));
    }

    @Test
    void shouldNotSendCountryToKafka2() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital(null);
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvc.perform(post("/kafka")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isBadRequest());
    }
}