package com.thegoodsamaritan.person.mapper;

import com.thegoodsamaritan.person.dto.CreatePersonRequest;
import com.thegoodsamaritan.person.dto.PersonResponse;
import com.thegoodsamaritan.person.entity.PersonEntity;
import com.thegoodsamaritan.person.entity.PersonRole;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonResponse toDto(PersonEntity entity) {
        return new PersonResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole()
        );
    }

    public PersonEntity newEntity(CreatePersonRequest request) {
        PersonEntity entity = new PersonEntity();
        entity.setName(request.name());
        entity.setEmail(request.email().trim().toLowerCase());
        entity.setRole(request.role());
        return entity;
    }

    public void applyUpdate(PersonEntity entity, String name, String email, PersonRole role) {
        entity.setName(name);
        entity.setEmail(email.trim().toLowerCase());
        entity.setRole(role);
    }
}
