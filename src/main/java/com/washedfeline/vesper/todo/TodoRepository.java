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

    public Todo getTodo(String id) {
        for (Todo todo : todos) {
            if (todo.id.equals(id)) {
                return todo;
            }
        }
        return null;
    }

    public Todo addTodo(Todo todo) {
        final Todo newTodo = new Todo(String.valueOf(arrayCounter), todo.title, todo.isCompleted);

        todos.add(newTodo);

        arrayCounter++;

        return newTodo;
    }

    public Todo updateTodo(Todo todo) {
        final int index = IntStream.range(0, todos.size())
                .filter(i -> todos.get(i).id.equals(todo.id))
                .findFirst()
                .orElse(-1);
        if (index != -1) {
            todos.set(index, todo);
            return todo;
        }
        return null;
    }

    public void deleteTodo(String id) {
        final Todo todoToDelete = getTodo(id);
        if (todoToDelete != null) {
            todos.remove(todoToDelete);
        }
    }
}
