package com.washedfeline.vesper.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TodoServiceTest {
    @MockBean
    private TodoRepository todoRepository;
    private TodoService todoService;

    @BeforeEach
    void init() {
        todoService = new TodoService(todoRepository);
    }

    @Nested
    class GetAllTodos {
        @Test
        void shouldReturnAllTodos() {
            final List<Todo> mockedTodos = List.of(
                    new Todo("0", "Take my dog for a walk", false),
                    new Todo("1", "Clean the house", true),
                    new Todo("2", "Wash clothes", false)
            );

            when(todoRepository.findAll()).thenReturn(mockedTodos);

            final List<Todo> actual = todoService.getAllTodos();

            verify(todoRepository, times(1)).findAll();

            assertEquals(mockedTodos, actual);
        }
    }

    @Nested
    class GetTodoById {
        @Test
        void shouldReturnTodo() {
            final Todo todo = new Todo("0", "Take my dog for a walk", false);

            when(todoRepository.findById("0")).thenReturn(Optional.of(todo));

            final Optional<Todo> actual = todoService.getTodo("0");

            verify(todoRepository, times(1)).findById("0");

            assertTrue(actual.isPresent());
            assertEquals(todo, actual.get());
        }
    }

    @Nested
    class AddTodo {
        @Test
        void shouldAddTodo() {
            final Todo todo = new Todo("0", "Take my dog for a walk", false);

            when(todoRepository.save(any(Todo.class))).thenReturn(todo);

            final Todo todoToAdd = new Todo(null, todo.title, todo.isCompleted);

            final Todo actual = todoService.addTodo(todoToAdd);

            verify(todoRepository, times(1)).save(todoToAdd);

            assertInstanceOf(String.class, actual.id);
            assertEquals(todo.title, actual.title);
            assertEquals(todo.isCompleted, actual.isCompleted);
        }
    }

    @Nested
    class UpdateTodo {
        @Test
        void shouldUpdateTodo() {
            final Todo todo = new Todo("0", "Visit family on this weekend", true);

            when(todoRepository.save(any(Todo.class))).thenReturn(todo);

            final Todo actual = todoService.updateTodo(todo);

            verify(todoRepository, times(1)).save(todo);

            assertEquals(todo, actual);
        }
    }

    @Nested
    class DeleteTodo {
        @Test
        void shouldDeleteTodo() {
            todoService.deleteTodo("0");

            verify(todoRepository, times(1)).deleteById("0");
        }
    }
}
