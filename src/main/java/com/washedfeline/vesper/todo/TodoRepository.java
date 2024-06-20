package com.washedfeline.vesper.todo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class TodoRepository {
    private final ArrayList<Todo> todos;
    private int arrayCounter;

    TodoRepository() {
        todos = new ArrayList<>();
        arrayCounter = 0;
    }

    public List<Todo> getAllTodos() {
        return todos;
    }

    public Todo getTodo(String id) throws TodoNotFoundException {
        final int index = IntStream.range(0, todos.size())
                .filter(i -> todos.get(i).id.equals(id))
                .findFirst()
                .orElseThrow(() -> new TodoNotFoundException("Todo with id " + id + " not found"));

        return todos.get(index);
    }

    public Todo addTodo(Todo todo) {
        final Todo newTodo = new Todo(String.valueOf(arrayCounter), todo.title, todo.isCompleted);

        todos.add(newTodo);

        arrayCounter++;

        return newTodo;
    }

    public Todo updateTodo(Todo todo) throws TodoNotFoundException {
        final int index = IntStream.range(0, todos.size())
                .filter(i -> todos.get(i).id.equals(todo.id))
                .findFirst()
                .orElseThrow(() -> new TodoNotFoundException("Todo with id " + todo.id + " not found"));

        return todos.set(index, todo);
    }

    public void deleteTodo(String id) throws TodoNotFoundException {
        final Todo todo = getTodo(id);

        todos.remove(todo);
    }
}
