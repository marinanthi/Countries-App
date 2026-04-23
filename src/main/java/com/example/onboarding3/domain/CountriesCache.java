package com.example.onboarding3.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "countryName",
        "countryCode",
        "capital",
        "continent",
        "officialLanguage",
        "currencyName"
})

@Data
@Getter
@Setter
@NoArgsConstructor
@Document(collection = "CountriesCache")
public class CountriesCache extends Country implements Serializable {
    @Indexed(name = "expireAt", expireAfter = "10")
    private final Instant createdAt = Instant.now();

    @NotBlank(message = "Country Name cannot be blank")
    private String countryName;

    @Id
    private String countryCode;

    public CountriesCache(String countryName, String countryCode, String capital, String continent, String officialLanguage, String currencyName) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.capital = capital;
        this.continent = continent;
        this.officialLanguage = officialLanguage;
        this.currencyName = currencyName;
    }
}
