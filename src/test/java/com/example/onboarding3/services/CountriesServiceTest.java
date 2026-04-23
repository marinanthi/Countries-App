package com.example.onboarding3.services;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.domain.Country;
import com.example.onboarding3.repositories.CountriesMongoRepo;
import com.example.onboarding3.restclients.CountriesRestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class CountriesServiceTest {

    @MockitoBean
    CountriesService countriesService;

    @MockitoBean
    CountriesRestClient restCountries;

    @MockitoBean
    CountriesMongoRepo countriesRepo;

    @Test
    void testFindByCapitalSuccess() throws Exception{
//        CountriesCache country = new CountriesCache("Greece", "Athens", "GR", "Europe", "Greek", "euro");
        Mockito.when(countriesService.findByCapital("Athens")).thenReturn(new CountriesCache("Greece", "Athens", "GR", "Europe", "Greek", "euro"));
        CountriesCache result = countriesService.findByCapital("Athens");

        assertEquals("Greece", result.getCountryName());
        verify(countriesService, times(1)).findByCapital("Athens");
    }

    @Test
    void testFindByNameSuccess() throws Exception{
        Mockito.when(countriesService.findByName("Greece")).thenReturn(new CountriesCache("Greece", "Athens", "GR", "Europe", "Greek", "euro"));
        CountriesCache result = countriesService.findByName("Greece");

        assertEquals("Greece", result.getCountryName());
        verify(countriesService, times(1)).findByCapital("Athens");
    }
}