package com.ritk.contact.services.user;

import com.ritk.contact.entity.User;
import com.ritk.contact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user){
        return userRepository.save(user);
    }

    @Override
    public Optional<User>findByPhoneNumber(String phone){
        return userRepository.findByPhoneNumber(phone);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);

    }
    @Override
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User Not Found with ID :"+id));
    }

    @Override
    public User updateUserProfile(Long userId,User updatedUser){
        User existingUser = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found with id: "+userId));

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
