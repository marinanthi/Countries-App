package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.services.BaseIntTestClass;
import com.example.onboarding3.services.CountriesService;
import com.example.onboarding3.services.KafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class KafkaControllerTest extends BaseIntTestClass {

    @MockitoBean
    KafkaService kafkaService;

    @Autowired
    MockMvc mockMvcKafka;

    @MockitoBean
    CountriesService countriesService;

    ObjectMapper objectMapper = new ObjectMapper();

    //  POST /kafka
    @Test
    void shouldSendCountry_ToKafka() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital("Athens");
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvcKafka.perform(post("/kafka")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isOk());

        Mockito.verify(kafkaService, times(1))
                .sendCountry(any(KafkaCountries.class));
    }

    @Test
    void shouldNotSendCountry_ToKafka() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital(null);
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvcKafka.perform(post("/kafka")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"capital\": null}"))
                .andExpect(status().isBadRequest());
    }

    //  GET /kafka/{countryName}
    @Test
    void shouldReturnCountry_fromDatabase() throws Exception {
        Mockito.when(kafkaService.getCountry("Greece")).thenReturn(new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvcKafka.perform(get("/kafka/Greece"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].countryName").isNotEmpty(),
                        jsonPath("$[0].countryCode").value("GR")
                );
    }

    // POST /kafka2
    @Test
    void shouldProduceAndConsume() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital("Athens");
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvcKafka.perform(post("/kafka2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country)))
                .andExpect(status().isOk());

        Mockito.verify(kafkaService, times(1))
                .sendCountryToKafka(any(KafkaCountries.class));
    }

    @Test
    void shouldNotProduceAndConsume() throws Exception {
        KafkaCountries country = new KafkaCountries();
        country.setCountryName("Greece");
        country.setCapital(null);
        country.setContinent("Europe");
        country.setCountryCode("GR");
        country.setOfficialLanguage("Greek");
        country.setCurrencyName("Euro");

        Mockito.doNothing().when(kafkaService).sendCountry(any(KafkaCountries.class));

        mockMvcKafka.perform(post("/kafka2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"capital\": null}"))
                .andExpect(status().isBadRequest());

//        mockMvcKafka.perform(post("/kafka")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(country)))
//                .andExpect(status().isBadRequest());
    }
}