package com.core.todo.controller;

import com.core.todo.exceptions.ResourceNotFoundException;
import com.core.todo.model.Task;
import com.core.todo.model.User;
import com.core.todo.services.TaskService;
import com.core.todo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

public class TodoController {
    private TaskService taskService;
    private UserService userService;


    @GetMapping("/tasks/{taskId}/users/{userId}")
    public ResponseEntity<?> getTaskById (@PathVariable long taskId,@PathVariable long userId) throws ResourceNotFoundException {
        Task task = taskService.getTask(taskId, userId);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/tasks/user/{userId}")
    public ResponseEntity<?> getUserTasks(@PathVariable long userId){
        Optional<User> user = userService.GetUserById(userId);

        if (user.isPresent()){
            List<Task> tasks = user.get().getTasks();
            return ResponseEntity.ok(tasks);
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");

    }
    @PostMapping("/tasks/user/{userId}")
    public ResponseEntity<Task> createTask(@PathVariable long userId, @PathVariable Task task)
            throws ResourceNotFoundException{
        try {
            Task createTask = taskService.addTask(userId, task);
            return ResponseEntity.status(HttpStatus.CREATED).body(createTask);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @PutMapping("/tasks/{taskId}/user/{userId}")
    public ResponseEntity<?> updateTask (@PathVariable long userId, @PathVariable long taskId, @RequestBody Task task)
            throws ResourceNotFoundException{
        taskService.updateTask(userId, taskId, task);
        return ResponseEntity.ok("task updated");
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable long taskId)throws ResourceNotFoundException{
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("task deleted");
    }


}
