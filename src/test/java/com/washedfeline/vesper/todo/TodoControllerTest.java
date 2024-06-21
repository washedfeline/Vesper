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
import static org.mockito.Mockito.doThrow;
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
    void setUp() throws Exception {
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

        @Test
        public void shouldReturnNotFoundWhenTodoDoesNotExist() throws Exception {
            when(todoService.getTodo(anyString())).thenThrow(new TodoNotFoundException("Todo not found"));

            mockMvc.perform(get("/todos/1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /todos")
    class CreateTodo {
        @Test
        public void shouldCreateNewTodo() throws Exception {
            Todo newTodo = new Todo("4", "Test Todo D", false);

            when(todoService.addTodo(any())).thenReturn(newTodo);

            mockMvc.perform(post("/todos")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(newTodo)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(content().json(objectMapper.writeValueAsString(newTodo)));
        }
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

        @Test
        public void shouldReturnNotFoundWhenTodoDoesNotExist() throws Exception {
            Todo oldTodo = expectedTodos.getFirst();
            Todo updatedTodo = new Todo(oldTodo.id, "Updated Todo", true);

            when(todoService.updateTodo(any())).thenThrow(new TodoNotFoundException("Todo not found"));

            mockMvc.perform(patch("/todos")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(updatedTodo)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /todos/{id}")
    class DeleteTodo {
        @Test
        public void shouldDeleteTodo() throws Exception {
            mockMvc.perform(delete("/todos/1")).andExpect(status().isNoContent());
        }

        @Test
        public void shouldReturnNotFoundWhenTodoDoesNotExist() throws Exception {
            doThrow(new TodoNotFoundException("Todo not found")).when(todoService).deleteTodo(anyString());

            mockMvc.perform(delete("/todos/1")).andExpect(status().isNotFound());
        }
    }
}
