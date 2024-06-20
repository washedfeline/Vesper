package com.washedfeline.vesper.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TodoService todoService;

    private final List<Todo> expectedTodos = List.of(
            new Todo("1", "Test Todo A", false),
            new Todo("2", "Test Todo B", true),
            new Todo("3", "Test Todo C", false)
    );

    @BeforeEach
    public void setUp() {
        when(todoService.getAllTodos()).thenReturn(expectedTodos);
        when(todoService.getTodo(anyString())).thenReturn(expectedTodos.getFirst());
    }

    @Nested
    @DisplayName("GET /todos")
    class GetAllTodos {
        @Test
        public void shouldReturnEmptyListWhenTodosAreEmpty() throws Exception {
            when(todoService.getAllTodos()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/todos"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
        }

        @Test
        public void shouldReturnAllTodos() throws Exception {
            mockMvc.perform(get("/todos"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedTodos)));
        }
    }

    @Nested
    @DisplayName("GET /todos/{id}")
    class GetTodoById {
        @Test
        public void shouldReturnTodoById() throws Exception {
            mockMvc.perform(get("/todos/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedTodos.getFirst())));
        }

        // TODO: Test it should return 404 if Todo is not found.
    }

    @Nested
    @DisplayName("POST /todos")
    class CreateTodo {
        @Test
        // TODO: It should return 201 instead of 200.
        public void shouldCreateNewTodo() throws Exception {
            Todo newTodo = new Todo("4", "Test Todo D", false);

            when(todoService.addTodo(any())).thenReturn(newTodo);

            mockMvc.perform(post("/todos")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(newTodo)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(objectMapper.writeValueAsString(newTodo)));
        }

        // TODO: Test it should return 400 if Todo is not valid/correct.
    }

    @Nested
    @DisplayName("PATCH /todos/{id}")
    class UpdateTodo {
        @Test
        public void shouldUpdateTodo() throws Exception {
            Todo oldTodo = expectedTodos.getFirst();
            Todo updatedTodo = new Todo(oldTodo.id, "Updated Todo", true);

            when(todoService.updateTodo(any())).thenReturn(updatedTodo);

            mockMvc.perform(patch("/todos")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(updatedTodo)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(objectMapper.writeValueAsString(updatedTodo)));
        }

        // TODO: Test it should return 400 if Todo is not valid/correct.

        // TODO: Test it should return 404 if Todo is not found.
    }

    @Nested
    @DisplayName("DELETE /todos/{id}")
    class DeleteTodo {
        @Test
        // TODO: It should return 204 instead of 200.
        public void shouldDeleteTodo() throws Exception {
            mockMvc.perform(delete("/todos/1"))
                    .andExpect(status().isOk());
        }

        // TODO: Test it should return 404 if Todo is not found.
    }
}
