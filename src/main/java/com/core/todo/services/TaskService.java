package com.core.todo.services;

import com.core.todo.dto.TaskDTO;
import com.core.todo.exceptions.ResourceNotFoundException;
import com.core.todo.model.Task;
import com.core.todo.model.User;
import com.core.todo.repository.TaskRepository;
import com.core.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    public Page<Task> getTasksByUserAndStatus(Long userId, String status, Pageable pageable){
        return taskRepository.findByUserIdAndStatusContainingIgnoreCase(userId, status, pageable);
    }



    public Task getTask(long taskId, long userId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow( () -> new ResourceNotFoundException("not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        if (task.getUser() == null || task.getUser().getId() != userId) {
            throw new ResourceNotFoundException("This task does not belong to the user");
        }

        return task;
    }

    public Task addTask(long userId, TaskDTO taskDTO) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setStatus(taskDTO.getStatus());
            task.setDescription(taskDTO.getDescription());
            task.setUser(user);

            return taskRepository.save(task);
        } else {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
    }


    public Task updateTask (long userId, long taskId, Task updateTaskDetails){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        task.setDescription(updateTaskDetails.getDescription());
        task.setStatus(updateTaskDetails.getStatus());
        task.setUser(user);

        return taskRepository.save(task);
    }

    public void deleteTask(long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("task not found"));

        taskRepository.deleteById(taskId);
    }
  }
