package com.thegoodsamaritan.person.service;

import com.thegoodsamaritan.person.dto.CreatePersonRequest;
import com.thegoodsamaritan.person.dto.PersonResponse;
import com.thegoodsamaritan.person.dto.UpdatePersonRequest;
import com.thegoodsamaritan.person.entity.PersonRole;
import com.thegoodsamaritan.person.exception.EmailConflictException;
import com.thegoodsamaritan.person.exception.PersonNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Test
    void createFindAllUpdateDelete() {
        PersonResponse created = personService.create(
                new CreatePersonRequest("Taylor", "taylor@example.com", PersonRole.VOLUNTEER));

        assertThat(created.id()).isNotNull();
        assertThat(created.email()).isEqualTo("taylor@example.com");

        assertThat(personService.findAll()).hasSize(1);

        PersonResponse updated = personService.update(
                created.id(),
                new UpdatePersonRequest("Taylor Lee", "taylor.lee@example.com", PersonRole.PROFESSIONAL));

        assertThat(updated.name()).isEqualTo("Taylor Lee");
        assertThat(updated.role()).isEqualTo(PersonRole.PROFESSIONAL);

        personService.delete(created.id());
        assertThat(personService.findAll()).isEmpty();
    }

    @Test
    void duplicateEmailOnCreate() {
        personService.create(new CreatePersonRequest("A", "dup@example.com", PersonRole.BENEFICIARY));

        assertThatThrownBy(() ->
                personService.create(new CreatePersonRequest("B", "dup@example.com", PersonRole.VOLUNTEER)))
                .isInstanceOf(EmailConflictException.class);
    }

    @Test
    void findByIdNotFound() {
        assertThatThrownBy(() -> personService.findById(UUID.randomUUID()))
                .isInstanceOf(PersonNotFoundException.class);
    }
}
