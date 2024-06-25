package com.washedfeline.vesper.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping(path = "{todoId}")
    public Todo getTodo(@PathVariable("todoId") String id) {
        try {
            return todoService.getTodo(id);
        } catch (TodoNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) {
        return new ResponseEntity<>(todoService.addTodo(todo), HttpStatus.CREATED);
    }

    @PatchMapping
    public Todo updateTodo(@RequestBody Todo todo) {
        try {
            return todoService.updateTodo(todo);
        } catch (TodoNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping(path = "{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("todoId") String id) {
        try {
            todoService.deleteTodo(id);

            return ResponseEntity.noContent().build();
        } catch (TodoNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
