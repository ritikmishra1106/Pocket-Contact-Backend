//Spring Security ke liye
package com.ritk.contact.security;

import com.ritk.contact.entity.User;
import com.ritk.contact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByPhoneNumber(username)
                .orElseThrow(
                        ()-> new UsernameNotFoundException("User not found with phone number: "+username));
        return new org.springframework.security.core.userdetails.User(
          user.getPhoneNumber(),
          user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
//Note:
//UserDetailsService Spring Security ka ek interface hai jiska kaam hota hai:
//        ➡️ Username (ya phone/email) ke basis par user ki detail database se uthana.
//        ➡️ Ye Spring Security ko batata hai ki is username ka user hai ya nahi.