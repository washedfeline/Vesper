package com.washedfeline.vesper.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.getAllTodos();
    }

    public Todo getTodo(String id) throws TodoNotFoundException {
        return todoRepository.getTodo(id);
    }

    public Todo addTodo(Todo todo) {
        return todoRepository.addTodo(todo);
    }

    public Todo updateTodo(Todo todo) throws TodoNotFoundException {
        return todoRepository.updateTodo(todo);
    }

    public void deleteTodo(String id) throws TodoNotFoundException {
        todoRepository.deleteTodo(id);
    }
}
