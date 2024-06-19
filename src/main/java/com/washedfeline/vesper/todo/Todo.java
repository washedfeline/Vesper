package com.washedfeline.vesper.todo;

public class Todo {
    public final String id;
    public final String title;
    public final boolean isCompleted;

    public Todo(String id, String title, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
    }
}
