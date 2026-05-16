package com.thegoodsamaritan.person.dto;

import com.thegoodsamaritan.person.entity.PersonRole;

import java.util.UUID;

public record PersonResponse(UUID id, String name, String email, PersonRole role) {
}
