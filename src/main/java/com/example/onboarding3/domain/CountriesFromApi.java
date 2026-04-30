package com.example.onboarding3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountriesFromApi {
    @JsonProperty("cca2")
    private String cca2;

    @JsonProperty("name")
    private Name name;

    @JsonProperty("capital")
    private List<String> capital;

    @JsonProperty("continents")
    private List<String> continents;

    @JsonProperty("languages")
    private Map<String, String> languages;

    @JsonProperty("currencies")
    private Map<String, CurrencyDetails> currencies;

    //  nested classes
    public static class Name {
        @JsonProperty("common")
        private String common;

        public Name(String common) {
            this.common = common;
        }

        public String getCommon() {
            return common;
        }
    }

    public static class CurrencyDetails{
        @JsonProperty("name")
        private String name;

        public CurrencyDetails(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static CountriesCache mapToCountry(CountriesFromApi country) {
        String mappedName = country.getName().getCommon();
        String mappedCapital = country.getCapital().get(0);
        String mappedContinents = country.getContinents().get(0);
        String officialLanguage = country.getLanguages().values().stream().toList().get(0);
        String currencyName = country.getCurrencies().values().stream().toList().get(0).getName();
        String cca2 = country.getCca2();
        return new CountriesCache(mappedName, cca2, mappedCapital, mappedContinents, officialLanguage, currencyName);
    }
}