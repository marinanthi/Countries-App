package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.services.BaseIntTestClass;
import com.example.onboarding3.services.RedisCacheService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RedisCacheControllerTest extends BaseIntTestClass {

    @MockitoBean
    RedisCacheService redisCacheService;

    @Autowired
    MockMvc mockMvcRedis;

    // GET /findByCapital/{capitalName}
    @Test
    void findByCapitalTest() throws Exception {
        Mockito.when(redisCacheService.getByCapital("Athens")).thenReturn(new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvcRedis.perform(get("/findByCapital/Athens"))
                .andExpect(status().isOk());
    }
}