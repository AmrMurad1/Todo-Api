package com.core.todo.repository;

import com.core.todo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByname (String name);
    Optional <User> findByEmail (String email);
    Optional<User> findById(long id);
}
