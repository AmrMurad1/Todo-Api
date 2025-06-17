package com.core.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String password;

    @OneToMany (mappedBy = "user" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

}
