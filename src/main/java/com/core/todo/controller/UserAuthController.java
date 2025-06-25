package com.core.todo.controller;

import com.core.todo.dto.UserLogin;
import com.core.todo.exceptions.ResourceNotFoundException;
import com.core.todo.model.User;
import com.core.todo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin credentials) throws ResourceNotFoundException{
        Map<String, String> response = userService.loginByUsername(credentials);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String resultMessage = userService.addNewUser(user);
        if (resultMessage.toLowerCase().contains("already")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(resultMessage);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
    }

}
