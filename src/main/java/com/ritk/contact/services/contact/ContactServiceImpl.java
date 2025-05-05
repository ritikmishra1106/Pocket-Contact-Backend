package com.ritk.contact.services.contact;

import com.ritk.contact.entity.Contact;
import com.ritk.contact.entity.User;
import com.ritk.contact.repository.ContactRepository;
import com.ritk.contact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Contact saveContactWithUser(Contact contact, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        contact.setUser(user);
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Contact contact){
        return contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Long id){
        contactRepository.deleteById(id);
    }

    @Override
    public Contact getContactById(Long id){
        return contactRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Contact Not Found"));
    }

    @Override
    public List<Contact> getAllContactsOfUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not Found"));
        return contactRepository.findByUser(user);
    }

}
