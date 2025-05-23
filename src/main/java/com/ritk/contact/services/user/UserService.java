package com.ritk.contact.services.user;

import com.ritk.contact.entity.User;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByPhoneNumber(String phone);
    Optional<User> findByEmail(String email);
    User getUserById(Long id);  // (Keep this if needed internally)
    User updateUserProfile(Long userId, User updatedUser); // (Keep if needed)

    // ✅ New methods for current user
    User getCurrentUser();
    User updateCurrentUserProfile(User updatedUser);
}
