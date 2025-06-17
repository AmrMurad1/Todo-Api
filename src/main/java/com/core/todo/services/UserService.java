package com.core.todo.services;
import com.core.todo.dto.UserLogin;
import com.core.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.core.todo.model.User;


@Service
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> GetUserById(Long id){
        return userRepository.findById(id);


    }
   public String RegisterNewUser(User user){
        Optional<User> ExistingByUsername = userRepository.findByUsername(user.getUsername());
        Optional<User> ExistingByEmail = userRepository.findByEmail(user.getEmail());

        if (ExistingByUsername.isPresent())
            return "Username already taken, choose another one";
        if (ExistingByEmail.isPresent())
            return "this Email already registered, choose another one";

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User saved successfully";

   }
   public Map<String, String> LoginByUserName(UserLogin credentials){
        String username = credentials.getUsername();
        String password =  credentials.getPassword();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("not found");

        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(username,password));

       Map<String, String> response = new HashMap<>();
       response.put("message", "Login successful");
       return response;

   }

   public User UpdateUser(Long id, User updateUser){
        User ExistingUser = userRepository.findById( id).
                orElseThrow(() -> new UsernameNotFoundException("user not found" + id));

        ExistingUser.setUsername(updateUser.getUsername());
        ExistingUser.setEmail(updateUser.getEmail());

       if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
           ExistingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
       }


   }


}

