package com.ritk.contact.controllers;

import com.ritk.contact.entity.User;
import com.ritk.contact.payload.ApiResponse;
import com.ritk.contact.payload.LoginResponse;
import com.ritk.contact.security.JwtUtil;
import com.ritk.contact.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Login - returns token
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getPhoneNumber(),
                        loginRequest.getPassword()
                )
        );
        String token = jwtUtil.generateToken(loginRequest.getPhoneNumber());
        LoginResponse response = new LoginResponse(token, loginRequest.getPhoneNumber());
        return ResponseEntity.ok(response);
    }

    // ✅ Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUserProfile() {
        User user = userService.getCurrentUser();
        ApiResponse<User> response = new ApiResponse<>("User profile fetched successfully", true, user);
        return ResponseEntity.ok(response);
    }

    // ✅ Update current user's profile
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<User>> updateProfile(@RequestBody User updateUser) {
        User updated = userService.updateCurrentUserProfile(updateUser);
        ApiResponse<User> response = new ApiResponse<>(
                "User profile updated successfully",
                true,
                updated
        );
        return ResponseEntity.ok(response);
    }
}
