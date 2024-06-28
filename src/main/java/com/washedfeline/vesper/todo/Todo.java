package com.washedfeline.vesper.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

@Entity
public class Todo {
    @Id
    @UuidGenerator
    public final String id;
    public final String title;
    public final Boolean isCompleted;

    public Todo() {
        id = null;
        title = null;
        isCompleted = null;
    }

    public Todo(String id, String title, Boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
    }
}
