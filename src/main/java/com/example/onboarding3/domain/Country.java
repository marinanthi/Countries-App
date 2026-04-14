package com.example.onboarding3.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country implements Serializable {
    @NotBlank(message = "Capital cannot be blank")
    protected String capital;

    @NotBlank(message = "Continent cannot be blank")
    protected String continent;

    @NotBlank(message = "Official Language cannot be blank")
    protected String officialLanguage;

    @NotBlank(message = "Currency Name cannot be blank")
    protected String currencyName;
}