package com.example.adaca.controller;

import com.example.adaca.model.Developer;
import com.example.adaca.service.DeveloperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeveloperController.class)
class DeveloperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeveloperService developerService;

    @Test
    void getAllDevelopers_returnsList() throws Exception {
        List<Developer> devs = List.of(
                Developer.builder().developerId(1).developerName("Alice").email("alice@test.com").build(),
                Developer.builder().developerId(2).developerName("Bob").email("bob@test.com").build()
        );
        when(developerService.getAllDevelopers()).thenReturn(devs);

        mockMvc.perform(get("/api/developers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].developerName").value("Alice"));
    }

    @Test
    void getAllDevelopers_returnsEmptyList() throws Exception {
        when(developerService.getAllDevelopers()).thenReturn(List.of());

        mockMvc.perform(get("/api/developers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
