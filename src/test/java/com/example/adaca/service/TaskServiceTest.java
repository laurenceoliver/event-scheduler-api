package com.example.adaca.service;

import com.example.adaca.dto.TaskDTO;
import com.example.adaca.model.Developer;
import com.example.adaca.model.Project;
import com.example.adaca.model.Task;
import com.example.adaca.repository.TaskRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private TaskExecutor taskExecutor;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_savesAndReturnsDTO() {
        TaskDTO dto = TaskDTO.builder()
                .name("Test Task")
                .priority(1)
                .dueDate(LocalDate.of(2026, 4, 1))
                .statusId(1)
                .developerId(1)
                .projectId(1)
                .build();

        Task savedTask = Task.builder()
                .id(1)
                .name("Test Task")
                .priority(1)
                .dueDate(LocalDate.of(2026, 4, 1))
                .status(1)
                .assignee(Developer.builder().developerId(1).build())
                .project(Project.builder().projectId(5).build())
                .build();

        when(taskRepo.save(any(Task.class))).thenReturn(savedTask);
        doAnswer(inv -> { inv.getArgument(0, Runnable.class).run(); return null; })
                .when(taskExecutor).execute(any(Runnable.class));

        TaskDTO result = taskService.createTask(5, dto);

        assertEquals("Test Task", result.getName());
        assertEquals(5, result.getProjectId());
        verify(taskRepo).save(any(Task.class));
        verify(emailService).sendEmail(eq("New taskDTO assigned!"), any(Task.class));
    }

    @Test
    void updateTask_updatesStatusAndProject() {
        Task existing = Task.builder()
                .id(1)
                .name("Existing")
                .priority(2)
                .dueDate(LocalDate.of(2026, 4, 1))
                .status(1)
                .assignee(Developer.builder().developerId(1).build())
                .project(Project.builder().projectId(1).build())
                .build();

        TaskDTO dto = TaskDTO.builder().id(1).statusId(3).build();

        when(taskRepo.findById(1)).thenReturn(Optional.of(existing));
        doAnswer(inv -> { inv.getArgument(0, Runnable.class).run(); return null; })
                .when(taskExecutor).execute(any(Runnable.class));

        TaskDTO result = taskService.updateTask(2, dto);

        assertEquals(3, result.getStatusId());
        assertEquals(2, result.getProjectId());
        verify(emailService).sendEmail(eq("Task updated!"), any(Task.class));
    }

    @Test
    void updateTask_throwsWhenNotFound() {
        TaskDTO dto = TaskDTO.builder().id(999).build();
        when(taskRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(1, dto));
    }

    @Test
    void getTasksByProject_defaultsDatesWhenNull() {
        Page<Task> emptyPage = new PageImpl<>(List.of());
        when(taskRepo.findByProject_ProjectIdAndDueDateBetween(
                anyInt(), any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        Page<TaskDTO> result = taskService.getTasksByProject(1, null, null, 0, 10, "dueDate");

        assertNotNull(result);
        verify(taskRepo).findByProject_ProjectIdAndDueDateBetween(
                eq(1), eq(LocalDate.now()), eq(LocalDate.now().plusDays(7)), any(Pageable.class));
    }

    @Test
    void getTasksByProject_usesProvidedDates() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 12, 31);

        Task task = Task.builder()
                .id(1).name("T").priority(1).dueDate(start).status(1)
                .assignee(Developer.builder().developerId(1).build())
                .project(Project.builder().projectId(1).build())
                .build();
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepo.findByProject_ProjectIdAndDueDateBetween(
                anyInt(), any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(page);

        Page<TaskDTO> result = taskService.getTasksByProject(1, start, end, 0, 10, "dueDate");

        assertEquals(1, result.getTotalElements());
        verify(taskRepo).findByProject_ProjectIdAndDueDateBetween(eq(1), eq(start), eq(end), any(Pageable.class));
    }
}
