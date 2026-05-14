package com.thegoodsamaritan.person;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class PersonStore {

    private final List<Person> people = new ArrayList<>();
    private final Object lock = new Object();

    public void add(Person person) {
        synchronized (lock) {
            people.add(person);
        }
    }

    public List<Person> all() {
        synchronized (lock) {
            return List.copyOf(people);
        }
    }

    public Person create(String name) {
        Person person = new Person(UUID.randomUUID(), name);
        add(person);
        return person;
    }
}
