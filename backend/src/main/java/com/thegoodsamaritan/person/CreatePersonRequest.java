package com.thegoodsamaritan.person;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatePersonRequest(@NotBlank(message = "name is required") String name) {
}
