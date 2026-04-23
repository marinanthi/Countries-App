package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.KafkaCountries;
import com.example.onboarding3.services.KafkaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KafkaControllerTest {
    @Autowired
    MockMvc mockMvcKafka;

    @MockitoBean
    KafkaService kafkaService;

    @Test
    void getKafkaCountry() throws Exception {
        Mockito.when(kafkaService.getCountry("Greece")).thenReturn(new KafkaCountries("Greece", "GR", "Athens", "Europe", "Greek", "Euro"));
        mockMvcKafka.perform(get("/kafka/Greece"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].countryName").value("Greece"),
                        jsonPath("$[0].countryCode").isNotEmpty()
                );

        MvcResult res = mockMvcKafka.perform(get("/kafka/Greece"))
                .andReturn();
        assertEquals(200, res.getResponse().getStatus());
    }
}