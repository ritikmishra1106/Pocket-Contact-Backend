package com.ritk.contact.controllers;

import com.ritk.contact.entity.User;
import com.ritk.contact.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

//    @PostMapping("/login")
//    public User loginUser(@RequestParam String phoneNumber, @RequestParam String password) {
//        return userService.findByPhoneNumber(phoneNumber)
//                .filter(user -> user.getPassword().equals(password))
//                .orElseThrow(() -> new RuntimeException("Invalid phone number or password"));
//    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User user){
        return userService.findByPhoneNumber(user.getPhoneNumber())
                .filter(u->u.getPassword().equals(user.getPassword()))
                .orElseThrow(()->new RuntimeException("Invalid phone number or password"));
    }


//    Profile

    @GetMapping("/{id}/profile")
    public ResponseEntity<User> getProfile(@PathVariable Long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

//    Update Profile
   @PutMapping("/{id}/profile")
    public ResponseEntity<User> updateProfile(@PathVariable Long id,@RequestBody User updateUser){
        User updated = userService.updateUserProfile(id,updateUser);
        return ResponseEntity.ok(updated);
    }
}
