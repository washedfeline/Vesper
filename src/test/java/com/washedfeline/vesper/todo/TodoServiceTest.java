package com.washedfeline.vesper.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class TodoServiceTest {
    @MockBean
    private TodoRepository todoRepository;
    private TodoService todoService;

    private final List<Todo> expectedTodos = List.of(
            new Todo("1", "Test Todo A", false),
            new Todo("2", "Test Todo B", true),
            new Todo("3", "Test Todo C", false)
    );

    @BeforeEach
    void setUp() throws Exception {
        todoService = new TodoService(todoRepository);

        when(todoRepository.getAllTodos()).thenReturn(expectedTodos);
        when(todoRepository.getTodo(anyString())).thenReturn(expectedTodos.getFirst());
    }

    @Nested
    @DisplayName("getAllTodos()")
    class GetAllTodos {
        @Test
        void shouldReturnEmptyListWhenTodosAreEmpty() {
            when(todoRepository.getAllTodos()).thenReturn(new ArrayList<>());

            List<Todo> actualTodos = todoService.getAllTodos();

            assertEquals(actualTodos.size(), 0);
        }

        @Test
        void shouldReturnAllTodos() {
            List<Todo> actualTodos = todoService.getAllTodos();

            assertEquals(expectedTodos, actualTodos);
        }
    }

    @Nested
    @DisplayName("getTodo(String id)")
    class GetTodoById {
        @Test
        void shouldThrowTodoNotFoundExceptionWhenTodoDoesNotExist() throws Exception {
            when(todoRepository.getTodo(anyString())).thenThrow(new TodoNotFoundException("Todo not found"));

            assertThrows(TodoNotFoundException.class, () -> todoService.getTodo("1"));

        }

        @Test
        void shouldReturnTodo() throws Exception {
            Todo actualTodo = todoService.getTodo("1");

            assertEquals(actualTodo, expectedTodos.getFirst());
        }
    }

    @Nested
    @DisplayName("addTodo(Todo todo)")
    class AddTodo {
        @Test
        void shouldAddTodo() {
            Todo todoToAdd = new Todo("1", "New Todo", false);
            when(todoRepository.addTodo(todoToAdd)).thenReturn(todoToAdd);

            Todo addedTodo = todoService.addTodo(todoToAdd);

            assertEquals(addedTodo, todoToAdd);
        }
    }

    @Nested
    @DisplayName("updateTodo(Todo todo)")
    class UpdateTodo {
        @Test
        void shouldUpdateTodo() throws Exception {
            Todo todoToUpdate = new Todo("1", "Updated Todo", true);
            when(todoRepository.updateTodo(todoToUpdate)).thenReturn(todoToUpdate);

            Todo updatedTodo = todoService.updateTodo(todoToUpdate);

            assertEquals(updatedTodo, todoToUpdate);
        }

        @Test
        void shouldThrowTodoNotFoundExceptionWhenTodoDoesNotExist() throws Exception {
            Todo todoToUpdate = new Todo("1", "Updated Todo", true);

            when(todoRepository.updateTodo(todoToUpdate)).thenThrow(new TodoNotFoundException("Todo not found"));

            assertThrows(TodoNotFoundException.class, () -> todoService.updateTodo(todoToUpdate));
        }
    }

    @Nested
    @DisplayName("deleteTodo(String id)")
    class DeleteTodo {
        @Test
        void shouldDeleteTodo() throws Exception {
            doNothing().when(todoRepository).deleteTodo("1");

            todoService.deleteTodo("1");
        }

        @Test
        void shouldThrowTodoNotFoundExceptionWhenTodoDoesNotExist() throws Exception {
            doThrow(new TodoNotFoundException("Todo not found")).when(todoRepository).deleteTodo(anyString());

            assertThrows(TodoNotFoundException.class, () -> todoService.deleteTodo("1"));

        }
    }
}
