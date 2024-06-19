package com.washedfeline.vesper.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return todoService.getTodo(id);
    }

    @PostMapping
    public Todo addTodo(@RequestBody Todo todo) {
        return todoService.addTodo(todo);
    }

    @PatchMapping
    public Todo updateTodo(@RequestBody Todo todo) {
        return todoService.updateTodo(todo);
    }
    @DeleteMapping(path = "{todoId}")
    public Todo deleteTodo(@PathVariable("todoId") String id) {
        return todoService.deleteTodo(id);
    }
}
