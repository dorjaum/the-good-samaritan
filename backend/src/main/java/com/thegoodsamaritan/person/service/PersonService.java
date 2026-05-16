package com.thegoodsamaritan.person.service;

import com.thegoodsamaritan.person.dto.CreatePersonRequest;
import com.thegoodsamaritan.person.dto.PersonResponse;
import com.thegoodsamaritan.person.dto.UpdatePersonRequest;
import com.thegoodsamaritan.person.entity.PersonEntity;
import com.thegoodsamaritan.person.exception.EmailConflictException;
import com.thegoodsamaritan.person.exception.PersonNotFoundException;
import com.thegoodsamaritan.person.mapper.PersonMapper;
import com.thegoodsamaritan.person.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PersonService {

    private final PersonRepository repository;
    private final PersonMapper mapper;

    public PersonService(PersonRepository repository, PersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<PersonResponse> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PersonResponse findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public PersonResponse create(CreatePersonRequest request) {
        String email = request.email().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new EmailConflictException();
        }
        PersonEntity entity = mapper.newEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public PersonResponse update(UUID id, UpdatePersonRequest request) {
        PersonEntity entity = repository.findById(id).orElseThrow(PersonNotFoundException::new);
        String email = request.email().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new EmailConflictException();
        }
        mapper.applyUpdate(entity, request.name(), email, request.role());
        return mapper.toDto(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new PersonNotFoundException();
        }
        repository.deleteById(id);
    }
}
