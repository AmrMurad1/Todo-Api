package com.core.todo.services;

import com.core.todo.dto.UserLogin;
import com.core.todo.model.User;
import com.core.todo.repository.UserRepository;
import com.core.todo.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final @Lazy AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Optional<User> GetUserById(Long id) {
        return userRepository.findById(id);
    }

    public String addNewUser(User user) {
        Optional<User> existUser = userRepository.findByUsername(user.getUsername());
        Optional<User> existEmail = userRepository.findByEmail(user.getEmail());

        if (existUser.isPresent())
            return "User already exist";

        if (existEmail.isPresent())
            return "This Email is taken";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User Saved Successfully";
    }

    public Map<String, String> loginByUsername(UserLogin credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = jwtService.GenerateToken(new HashMap<>(), user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    public User getAuthenticatedUser(HttpHeaders headers) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            throw new UsernameNotFoundException("User not authenticated");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User updateUser(Long id, User updateUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));

        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());

        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }
}
