package com.example.onboarding3.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@JsonPropertyOrder({
        "countryName",
        "countryCode",
        "capital",
        "continent",
        "officialLanguage",
        "currencyName"
})

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "KafkaCountries")
public class KafkaCountries extends Country implements Serializable {
    @Id
    private String countryName;

    @NotBlank(message = "Country Code cannot be blank")
    private String countryCode;

    public KafkaCountries(String countryName, String countryCode, String capital, String continent, String officialLanguage, String currencyName) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.capital = capital;
        this.continent = continent;
        this.officialLanguage = officialLanguage;
        this.currencyName = currencyName;
    }

    public String toString() {
        return "Country Name: " + countryName + ", Country Code: " + countryCode + ", Capital: " + capital + ", Continent: " + continent + ", Official Language: " + officialLanguage + ", Currency Name: " + currencyName;
    }
}
