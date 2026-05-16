package com.thegoodsamaritan.person.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thegoodsamaritan.person.entity.PersonRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatePersonRequest(
        @NotBlank(message = "name is required") @Size(max = 255) String name,
        @NotBlank(message = "email is required") @Email(message = "email must be valid") @Size(max = 320) String email,
        @NotNull(message = "role is required") PersonRole role
) {
}
