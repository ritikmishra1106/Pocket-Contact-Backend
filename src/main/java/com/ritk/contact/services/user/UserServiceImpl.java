package com.ritk.contact.services.user;

import com.ritk.contact.entity.User;
import com.ritk.contact.exception.GlobalExceptionHandler;
import com.ritk.contact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new GlobalExceptionHandler.EmailAlreadyExistsException("User already registered with this email");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phone) {
        return userRepository.findByPhoneNumber(phone);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found with ID :" + id));
    }

    @Override
    public User updateUserProfile(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return updateFields(existingUser, updatedUser);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // phoneNumber
        return userRepository.findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public User updateCurrentUserProfile(User updatedUser) {
        User currentUser = getCurrentUser();
        return updateFields(currentUser, updatedUser);
    }

    private User updateFields(User existingUser, User updatedUser) {
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setCompany(updatedUser.getCompany());
        existingUser.setJobTitle(updatedUser.getJobTitle());
        existingUser.setBiography(updatedUser.getBiography());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        return userRepository.save(existingUser);
    }
}
