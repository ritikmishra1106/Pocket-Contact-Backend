package com.ritk.contact.repository;

import com.ritk.contact.entity.Contact;
import com.ritk.contact.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Long> {
    List<Contact> findByUser(User user);

}
