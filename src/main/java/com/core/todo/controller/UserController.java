package com.core.todo.controller;

import com.core.todo.exceptions.ResourceNotFoundException;
import com.core.todo.model.User;
import com.core.todo.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private UserService userService;


    @GetMapping("/userProfile")
    public ResponseEntity<User> userProfile(@RequestHeader HttpHeaders headers)
            throws ResourceNotFoundException{
        return ResponseEntity.ok(userService.getAuthenticatedUser(headers));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable long userId,@RequestBody User updateUserData) throws ResourceNotFoundException{
        User updateUser = userService.updateUser(userId, updateUserData);
        return ResponseEntity.ok(updateUser);
    }

    public ResponseEntity<String> deleteUser (@PathVariable long userId) throws ResourceNotFoundException{
        userService.deleteUser(userId);
        return ResponseEntity.ok("user deleted");
    }
}
