//v2
package com.ecommerce.productapi.controller;

import com.ecommerce.productapi.model.User;
import com.ecommerce.productapi.service.UserService;
import com.ecommerce.productapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        // Validate user using email and password
        User user = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null) {
            // Generate JWT token if user is valid
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole()); // Pass both email and role
            return ResponseEntity.ok("Login successful, token: " + token);
        } else {
            // Return 401 Unauthorized if email or password is incorrect
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User registerRequest) {
        // Register new user
        User user = userService.registerUser(registerRequest.getEmail(), registerRequest.getPassword(), "USER");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole()); // Pass both email and role
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully, token: " + token);
    }
}
