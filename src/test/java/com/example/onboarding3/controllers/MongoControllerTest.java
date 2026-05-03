package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.services.CountriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
public class ControllerTest {
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

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CountriesService countriesService;

    //  GET /capital/{capitalName}
    @Test
    void testByCapitalSuccess() throws Exception{
        Mockito.when(countriesService.findByCapital("Athens")).thenReturn(new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvc.perform(get("/capital/Athens"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.countryName").isNotEmpty(),
                        jsonPath("$.countryCode").value("GR")
                );
    }

    @Test
    void shouldReturnCountry_whenCapitalExists() throws Exception {
        CountriesCache country = new CountriesCache();
        country.setCountryName("Greece");
        country.setCapital("Athens");
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.when(countriesService.findByCapital(anyString()))
                .thenReturn(country);

        mockMvc.perform(get("/capital/Athens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryName").value("Greece"))
                .andExpect(jsonPath("$.capital").value("Athens"))
                .andExpect(jsonPath("$.continent").value("Europe"))
                .andExpect(jsonPath("$.countryCode").value("GR"))
                .andExpect(jsonPath("$.officialLanguage").value("Greek"))
                .andExpect(jsonPath("$.currencyName").value("Euro"));

        Mockito.verify(countriesService, times(1))
                .findByCapital("Athens");
    }

    @Test
    void testByNameSuccess() throws Exception{
        Mockito.when(countriesService.findByName("Greece")).thenReturn(new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvc.perform(get("/country?countryName=Greece"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.countryName").isNotEmpty(),
                        jsonPath("$.countryCode").value("GR")
                );
    }
}