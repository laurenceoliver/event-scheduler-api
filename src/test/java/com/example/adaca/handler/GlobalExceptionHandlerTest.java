package com.example.adaca.handler;

import com.example.adaca.model.ErrorMsg;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUniqueConstraint_returns409WithErrorMsg() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("duplicate key");

        ResponseEntity<ErrorMsg> response = handler.handleUniqueConstraint(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("duplicate_task", response.getBody().getKey());
        assertEquals("A task with the same name already exists in this project.", response.getBody().getMessage());
    }
}
