package com.example.adaca.service;

import com.example.adaca.model.Developer;
import com.example.adaca.repository.DeveloperRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceTest {

    @Mock
    private DeveloperRepo developerRepo;

    @InjectMocks
    private DeveloperService developerService;

    @Test
    void getAllDevelopers_returnsList() {
        List<Developer> devs = List.of(
                Developer.builder().developerId(1).developerName("Alice").email("alice@test.com").build(),
                Developer.builder().developerId(2).developerName("Bob").email("bob@test.com").build()
        );
        when(developerRepo.findAll()).thenReturn(devs);

        List<Developer> result = developerService.getAllDevelopers();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getDeveloperName());
    }

    @Test
    void getAllDevelopers_returnsEmptyList() {
        when(developerRepo.findAll()).thenReturn(List.of());

        List<Developer> result = developerService.getAllDevelopers();

        assertTrue(result.isEmpty());
    }
}
