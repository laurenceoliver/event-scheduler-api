package com.example.adaca.model;

import com.example.adaca.dto.TaskDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void getTaskDTO_convertsCorrectly() {
        Task task = Task.builder()
                .id(1)
                .name("Task A")
                .priority(2)
                .dueDate(LocalDate.of(2026, 5, 1))
                .status(3)
                .assignee(Developer.builder().developerId(4).build())
                .project(Project.builder().projectId(7).build())
                .build();

        TaskDTO dto = task.getTaskDTO();

        assertEquals(1, dto.getId());
        assertEquals("Task A", dto.getName());
        assertEquals(2, dto.getPriority());
        assertEquals(LocalDate.of(2026, 5, 1), dto.getDueDate());
        assertEquals(3, dto.getStatusId());
        assertEquals(4, dto.getDeveloperId());
        assertEquals(7, dto.getProjectId());
    }
}
