package com.core.todo.repository;

import com.core.todo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long>{

    Page<Task> findByUserIdAndStatusContainingIgnoreCase(Long userId, String status, Pageable pageable);
}
