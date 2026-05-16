package com.thegoodsamaritan.person.mapper;

import com.thegoodsamaritan.person.dto.CreatePersonRequest;
import com.thegoodsamaritan.person.dto.PersonResponse;
import com.thegoodsamaritan.person.entity.PersonEntity;
import com.thegoodsamaritan.person.entity.PersonRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PersonMapperTest {

    private final PersonMapper mapper = new PersonMapper();

    @Test
    void toDto() {
        PersonEntity entity = new PersonEntity();
        entity.setId(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"));
        entity.setName("Sam");
        entity.setEmail("sam@example.com");
        entity.setRole(PersonRole.PROFESSIONAL);

        PersonResponse dto = mapper.toDto(entity);

        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.name()).isEqualTo("Sam");
        assertThat(dto.email()).isEqualTo("sam@example.com");
        assertThat(dto.role()).isEqualTo(PersonRole.PROFESSIONAL);
    }

    @Test
    void newEntityNormalizesEmail() {
        CreatePersonRequest req = new CreatePersonRequest("Jo", "  Jo@EXAMPLE.ORG ", PersonRole.BENEFICIARY);
        PersonEntity entity = mapper.newEntity(req);

        assertThat(entity.getName()).isEqualTo("Jo");
        assertThat(entity.getEmail()).isEqualTo("jo@example.org");
        assertThat(entity.getRole()).isEqualTo(PersonRole.BENEFICIARY);
    }
}
