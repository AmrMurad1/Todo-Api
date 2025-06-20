package com.core.todo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Entity
@Table (name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    private long id;

    private String description;

    private String status;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey (name = "fk_user_id")
    )

    private User user;
}
