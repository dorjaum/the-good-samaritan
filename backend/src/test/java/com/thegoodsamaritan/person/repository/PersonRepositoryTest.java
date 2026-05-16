package com.thegoodsamaritan.person.repository;

import com.thegoodsamaritan.person.entity.PersonEntity;
import com.thegoodsamaritan.person.entity.PersonRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveAndFindById() {
        PersonEntity p = new PersonEntity();
        p.setName("Alex");
        p.setEmail("alex@example.com");
        p.setRole(PersonRole.VOLUNTEER);
        PersonEntity saved = repository.save(p);
        entityManager.flush();
        entityManager.clear();

        assertThat(repository.findById(saved.getId())).isPresent()
                .hasValueSatisfying(e -> {
                    assertThat(e.getName()).isEqualTo("Alex");
                    assertThat(e.getEmail()).isEqualTo("alex@example.com");
                    assertThat(e.getRole()).isEqualTo(PersonRole.VOLUNTEER);
                });
    }

    @Test
    void existsByEmailIgnoreCase() {
        PersonEntity p = new PersonEntity();
        p.setName("A");
        p.setEmail("Mix@Example.com");
        p.setRole(PersonRole.BENEFICIARY);
        repository.save(p);
        entityManager.flush();

        assertThat(repository.existsByEmailIgnoreCase("mix@example.com")).isTrue();
        assertThat(repository.existsByEmailIgnoreCaseAndIdNot("mix@example.com", p.getId())).isFalse();
    }
}
