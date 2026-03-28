package com.example.adaca.service;

import com.example.adaca.model.Developer;
import com.example.adaca.model.Task;
import com.example.adaca.repository.DeveloperRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private DeveloperRepo developerRepo;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendEmail_logWhenDeveloperFound() {
        Developer dev = Developer.builder().developerId(1).developerName("Alice").email("alice@test.com").build();
        Task task = Task.builder().assignee(Developer.builder().developerId(1).build()).build();

        when(developerRepo.findById(1)).thenReturn(Optional.of(dev));

        emailService.sendEmail("Test Subject", task);

        verify(developerRepo).findById(1);
    }

    @Test
    void sendEmail_noOpWhenDeveloperNotFound() {
        Task task = Task.builder().assignee(Developer.builder().developerId(99).build()).build();

        when(developerRepo.findById(99)).thenReturn(Optional.empty());

        emailService.sendEmail("Test Subject", task);

        verify(developerRepo).findById(99);
    }
}
