package com.example.adaca.controller;

import com.example.adaca.dto.TaskDTO;
import com.example.adaca.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void create_returnsTask() throws Exception {
        TaskDTO input = TaskDTO.builder()
                .name("New Task").priority(1).dueDate(LocalDate.of(2026, 4, 1))
                .statusId(1).developerId(1).projectId(1).build();
        TaskDTO saved = TaskDTO.builder()
                .id(1).name("New Task").priority(1).dueDate(LocalDate.of(2026, 4, 1))
                .statusId(1).developerId(1).projectId(1).build();

        when(taskService.createTask(eq(1), any(TaskDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Task"));
    }

    @Test
    void update_returnsUpdatedTask() throws Exception {
        TaskDTO input = TaskDTO.builder().id(1).statusId(3).build();
        TaskDTO updated = TaskDTO.builder()
                .id(1).name("Task").statusId(3).projectId(2).developerId(1).build();

        when(taskService.updateTask(eq(2), any(TaskDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/tasks/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusId").value(3));
    }

    @Test
    void getTasksByProject_returnsPage() throws Exception {
        TaskDTO dto = TaskDTO.builder()
                .id(1).name("Task 1").priority(1).statusId(1).projectId(1).developerId(1).build();
        Page<TaskDTO> page = new PageImpl<>(List.of(dto));

        when(taskService.getTasksByProject(eq(1), isNull(), isNull(), eq(0), eq(100), eq("dueDate")))
                .thenReturn(page);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Task 1"))
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }

    @Test
    void getTasksByProject_withDateParams() throws Exception {
        Page<TaskDTO> page = new PageImpl<>(List.of());

        when(taskService.getTasksByProject(eq(1), eq(LocalDate.of(2026, 1, 1)), any(), eq(0), eq(10), eq("priority")))
                .thenReturn(page);

        mockMvc.perform(get("/api/tasks/1")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-12-31")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "priority"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(0));
    }
}
