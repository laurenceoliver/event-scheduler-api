package com.example.adaca.service;

import com.example.adaca.model.Project;
import com.example.adaca.repository.ProjectRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepo projectRepo;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAllProjects_returnsList() {
        List<Project> projects = List.of(
                Project.builder().projectId(1).projectName("Project A").build(),
                Project.builder().projectId(2).projectName("Project B").build()
        );
        when(projectRepo.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        assertEquals("Project A", result.get(0).getProjectName());
    }

    @Test
    void getAllProjects_returnsEmptyList() {
        when(projectRepo.findAll()).thenReturn(List.of());

        List<Project> result = projectService.getAllProjects();

        assertTrue(result.isEmpty());
    }
}
