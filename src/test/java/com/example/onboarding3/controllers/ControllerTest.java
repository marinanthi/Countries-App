package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.services.CountriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CountriesService countriesService;

    @Test
    void testByCapital() throws Exception{
        Mockito.when(countriesService.findByCapital("Athens")).thenReturn(new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvc.perform(get("/capital/Athens"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.countryName").isNotEmpty(),
                        jsonPath("$.countryCode").value("GR")
                );
    }

    @Test
    void testByName() throws Exception{
        Mockito.when(countriesService.findByName("Greece")).thenReturn(new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvc.perform(get("/country?countryName=Greece"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.countryName").isNotEmpty(),
                        jsonPath("$.countryCode").value("GR")
                );
    }
}