package com.example.onboarding3.domain;

import org.junit.jupiter.api.Test;
import java.util.*;
import static com.example.onboarding3.domain.CountriesFromApi.mapToCountry;
import static org.junit.jupiter.api.Assertions.*;

class CountriesFromApiTest {
    @Test
    void testMapToCountry() {
        CountriesFromApi countriesFromApi = CountriesFromApi.builder()
                .name(new CountriesFromApi.Name("Greece"))
                .cca2("GR")
                .capital(List.of("Athens"))
                .continents(List.of("Europe"))
                .languages(Map.of("1", "Greek"))
                .currencies(Map.of("2", new CountriesFromApi.CurrencyDetails("Euro")))
                .build();

        CountriesCache countriesCache = mapToCountry(countriesFromApi);

        assertNotNull(countriesCache);
        assertEquals("Greece", countriesCache.getCountryName());
        assertEquals("Athens", countriesCache.getCapital());
        assertEquals("GR", countriesCache.getCountryCode());
        assertEquals("Europe", countriesCache.getContinent());
        assertEquals("Greek", countriesCache.getOfficialLanguage());
        assertEquals("Euro", countriesCache.getCurrencyName());
    }
}