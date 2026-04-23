package com.example.onboarding3.restclients;

import com.example.onboarding3.domain.CountriesCache;
import com.example.onboarding3.domain.CountriesFromApi;
import com.example.onboarding3.domain.Country;
import com.example.onboarding3.domain.KafkaCountries;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

import static com.example.onboarding3.domain.CountriesFromApi.mapToCountry;

@Component
public class CountriesRestClient {

    private final RestTemplate rest;

    public CountriesRestClient(RestTemplate rest) {
        this.rest = rest;
    }

    public CountriesCache getByCapital(String capitalName) {
        CountriesFromApi[] country = rest.getForObject("https://restcountries.com/v3.1/capital/{capital}?fields=name,cca2,capital,continents,languages,currencies",
                CountriesFromApi[].class, capitalName);
        return mapToCountry(Arrays.stream(country).findFirst().orElse(null));
    }

    public CountriesCache getByName(String countryName) {
        try {
            CountriesFromApi[] country = rest.getForObject("https://restcountries.com/v3.1/name/{name}?fields=name,cca2,capital,continents,languages,currencies",
                    CountriesFromApi[].class, countryName);
            return mapToCountry(Arrays.stream(country).findFirst().orElse(null));
        } catch (Exception e) {
            throw new RuntimeException("There is no country whose name is " + countryName);
        }
    }
}