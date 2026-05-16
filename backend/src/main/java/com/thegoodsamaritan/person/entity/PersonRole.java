package com.thegoodsamaritan.person.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum PersonRole {
    BENEFICIARY,
    VOLUNTEER,
    PROFESSIONAL;

    @JsonValue
    public String toJson() {
        return name().toLowerCase(Locale.ROOT);
    }

    @JsonCreator
    public static PersonRole fromJson(String value) {
        if (value == null) {
            return null;
        }
        return PersonRole.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}
