package com.example.adaca.dto;

import com.example.adaca.model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    @Test
    void toTask_convertsCorrectly() {
        TaskDTO dto = TaskDTO.builder()
                .id(1)
                .name("Task A")
                .priority(3)
                .dueDate(LocalDate.of(2026, 6, 15))
                .statusId(2)
                .developerId(5)
                .projectId(10)
                .build();

        Task task = dto.toTask();

        assertEquals(1, task.getId());
        assertEquals("Task A", task.getName());
        assertEquals(3, task.getPriority());
        assertEquals(LocalDate.of(2026, 6, 15), task.getDueDate());
        assertEquals(2, task.getStatus());
        assertEquals(5, task.getAssignee().getDeveloperId());
        assertEquals(10, task.getProject().getProjectId());
    }

    @Test
    void testIdDefaultsTo104() {
        TaskDTO dto = TaskDTO.builder().name("X").build();
        assertEquals(104, dto.getTestId());
    }
}
