package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.services.CountriesService;
import com.example.onboarding3.services.RedisCacheService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RedisCacheControllerTest {
    @Autowired
    MockMvc mockMvc;

//    @MockitoBean
//    RedisCacheService countriesService;

    @Test
    void byCapital() throws Exception {
//        Mockito.when(countriesService.getByCapital("Athens")).thenReturn(new CountriesCache("Greece", "Athens", "GR", "Europe", "Greek", "euro"));
        mockMvc.perform(get("/findByCapital/Athens"))
                .andExpect(status().isOk());
    }
}