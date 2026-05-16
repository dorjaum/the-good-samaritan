package com.thegoodsamaritan.person.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegoodsamaritan.person.dto.CreatePersonRequest;
import com.thegoodsamaritan.person.dto.PersonResponse;
import com.thegoodsamaritan.person.dto.UpdatePersonRequest;
import com.thegoodsamaritan.person.entity.PersonRole;
import com.thegoodsamaritan.person.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Test
    void listReturnsJson() throws Exception {
        UUID id = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
        when(personService.findAll()).thenReturn(List.of(
                new PersonResponse(id, "Pat", "pat@example.com", PersonRole.BENEFICIARY)));

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("Pat"))
                .andExpect(jsonPath("$[0].email").value("pat@example.com"))
                .andExpect(jsonPath("$[0].role").value("beneficiary"));
    }

    @Test
    void createReturns201() throws Exception {
        UUID id = UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22");
        when(personService.create(ArgumentMatchers.any(CreatePersonRequest.class)))
                .thenReturn(new PersonResponse(id, "Pat", "pat@example.com", PersonRole.VOLUNTEER));

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreatePersonRequest("Pat", "pat@example.com", PersonRole.VOLUNTEER))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("volunteer"));

        verify(personService).create(ArgumentMatchers.any(CreatePersonRequest.class));
    }

    @Test
    void createInvalidBodyReturns400() throws Exception {
        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDelegatingToService() throws Exception {
        UUID id = UUID.fromString("c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33");
        when(personService.update(ArgumentMatchers.eq(id), ArgumentMatchers.any(UpdatePersonRequest.class)))
                .thenReturn(new PersonResponse(id, "Pat", "pat2@example.com", PersonRole.PROFESSIONAL));

        mockMvc.perform(put("/person/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new UpdatePersonRequest("Pat", "pat2@example.com", PersonRole.PROFESSIONAL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("pat2@example.com"))
                .andExpect(jsonPath("$.role").value("professional"));
    }

    @Test
    void deleteNoContent() throws Exception {
        UUID id = UUID.fromString("d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44");
        mockMvc.perform(delete("/person/" + id))
                .andExpect(status().isNoContent());
        verify(personService).delete(id);
    }
}
