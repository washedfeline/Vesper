package com.washedfeline.vesper.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TodoService todoService;

    @Nested
    @DisplayName("GET /todos")
    class GetAllTodos {
        @Test
        void shouldReturnAllTodos() throws Exception {
            final List<Todo> mockedTodos = List.of(
                    new Todo("0", "Take my dog for a walk", false),
                    new Todo("1", "Clean the house", true),
                    new Todo("2", "Wash clothes", false)
            );

            when(todoService.getAllTodos()).thenReturn(mockedTodos);

            mockMvc.perform(get("/todos"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(mockedTodos)));

            verify(todoService, times(1)).getAllTodos();
        }
    }

    @Nested
    @DisplayName("GET /todos/{id}")
    class GetTodoById {
        @Test
        void shouldReturnTodoById() throws Exception {
            final Todo todo = new Todo("0", "Take my dog for a walk", false);

            when(todoService.getTodo(anyString())).thenReturn(Optional.of(todo));

            mockMvc.perform(get("/todos/0"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(todo)));

            verify(todoService, times(1)).getTodo("0");
        }

        @Test
        void shouldReturnNotFoundWhenTodoIsEmpty() throws Exception {
            when(todoService.getTodo(anyString())).thenReturn(Optional.empty());

            mockMvc.perform(get("/todos/0"))
                    .andExpect(status().isNotFound());

            verify(todoService, times(1)).getTodo("0");
        }
    }

    @Nested
    @DisplayName("POST /todos")
    class AddTodo {
        @Test
        void shouldAddTodo() throws Exception {
            final Todo responseTodo = new Todo("0", "Take my dog for a walk", false);

            final Todo requestTodo = new Todo(null, responseTodo.title, responseTodo.isCompleted);

            when(todoService.addTodo(any(Todo.class))).thenReturn(responseTodo);

            mockMvc.perform(post("/todos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestTodo)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(responseTodo)));

            verify(todoService, times(1)).addTodo(any(Todo.class));
        }
    }

    @Nested
    @DisplayName("PATCH /todos")
    class UpdateTodo {
        @Test
        void shouldUpdateTodo() throws Exception {
            final Todo todo = new Todo("0", "Wash clothes", true);

            when(todoService.updateTodo(any(Todo.class))).thenReturn(todo);

            mockMvc.perform(patch("/todos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(todo)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(todo)));

            verify(todoService, times(1)).updateTodo(any(Todo.class));
        }
    }

    @Nested
    @DisplayName("DELETE /todos")
    class DeleteTodo {
        @Test
        void shouldDeleteTodo() throws Exception {
            final String todoId = "0";

            mockMvc.perform(delete("/todos/{id}", todoId))
                    .andExpect(status().isNoContent());

            verify(todoService, times(1)).deleteTodo(todoId);
        }
    }
}
