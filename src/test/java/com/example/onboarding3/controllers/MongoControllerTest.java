package com.example.onboarding3.controllers;

import com.example.onboarding3.domain.CountriesCache;

import com.example.onboarding3.services.BaseIntTestClass;
import com.example.onboarding3.services.CountriesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MongoControllerTest extends BaseIntTestClass{

    @Autowired
    MockMvc mockMvcMongo;

    @MockitoBean
    CountriesService countriesService;

    //  GET /capital/{capitalName}
    @Test
    void testByCapitalSuccess() throws Exception{
        Mockito.when(countriesService.findByCapital("Athens")).thenReturn(new CountriesCache("Greece", "GR", "Athens", "Europe", "Greek", "euro"));
        mockMvcMongo.perform(get("/capital/Athens"))
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

        mockMvcMongo.perform(get("/capital/Athens"))
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
        mockMvcMongo.perform(get("/country?countryName=Greece"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.countryName").isNotEmpty(),
                        jsonPath("$.countryCode").value("GR")
                );
    }
}