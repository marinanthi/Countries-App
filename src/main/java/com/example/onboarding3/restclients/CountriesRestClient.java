package com.example.onboarding3.restclients;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.domain.CountriesFromApi;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class CountriesRestClient {

    private final RestTemplate rest;
    private final CountriesFromApi countries;

    public CountriesRestClient(RestTemplate rest, CountriesFromApi countries) {
        this.rest = rest;
        this.countries = countries;
    }

    public CountriesCache getByCapital(String capitalName) {
            CountriesFromApi[] country = rest.getForObject("https://restcountries.com/v3.1/capital/{capital}?fields=name,cca2,capital,continents,languages,currencies",
                    CountriesFromApi[].class, capitalName);
            return countries.mapToCountry(Arrays.stream(country).findFirst().orElse(null));
    }

    public CountriesCache getByName(String countryName) {
        try {
            CountriesFromApi[] country = rest.getForObject("https://restcountries.com/v3.1/name/{name}?fields=name,cca2,capital,continents,languages,currencies",
                    CountriesFromApi[].class, countryName);
            return countries.mapToCountry(Arrays.stream(country).findFirst().orElse(null));
        } catch (Exception e) {
            throw new RuntimeException("There is no country whose name is " + countryName);
        }
    }
}