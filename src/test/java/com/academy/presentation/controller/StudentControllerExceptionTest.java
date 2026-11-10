package com.academy.presentation.controller;

import com.academy.business.exception.StudentEmailAlreadyExistsException;
import com.academy.business.exception.StudentNotFoundException;
import com.academy.business.service.StudentService;
import com.academy.presentation.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudentControllerExceptionTest {

    private MockMvc mockMvc;
    private StudentService service;

    @BeforeEach
    void setUp() {
        service = mock(StudentService.class);
        StudentController controller = new StudentController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnStructuredNotFoundError() throws Exception {
        doThrow(StudentNotFoundException.byId(999L)).when(service).findById(999L);

        mockMvc.perform(get("/api/v1/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No existe un estudiante con id 999"))
                .andExpect(jsonPath("$.path").value("/api/v1/students/999"));
    }

    @Test
    void shouldReturnStructuredConflictError() throws Exception {
        doThrow(new StudentEmailAlreadyExistsException("ana@mail.com")).when(service).create(any());

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ana\",\"age\":22,\"email\":\"ana@mail.com\",\"status\":\"ACTIVE\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("El correo ana@mail.com ya está registrado"));
    }
}
