package com.ritk.contact.services.contact;

import com.ritk.contact.entity.Contact;
import com.ritk.contact.entity.User;
import com.ritk.contact.exception.GlobalExceptionHandler;
import com.ritk.contact.repository.ContactRepository;
import com.ritk.contact.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserService userService;

    // ✅ Save contact for current user
    @Override
    public Contact saveContactForCurrentUser(Contact contact) {
        User currentUser = userService.getCurrentUser();

        // 👇 Duplicate check
        Optional<Contact> existing = contactRepository.findByUserAndPhoneNumber(currentUser, contact.getPhoneNumber());

        if (existing.isPresent()) {
            throw new RuntimeException("Contact already exists with this phone number.");
        }

        contact.setUser(currentUser);
        return contactRepository.save(contact);
    }


    // ✅ Update contact (only if current user is owner)
    @Override
    public Contact updateContact(Long contactId, Contact updateContact) {
        User currentUser = userService.getCurrentUser();
        Contact existing = contactRepository.findById(contactId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Contact not found with id: " + contactId));

        if (!existing.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized to update this contact");
        }

        existing.setName(updateContact.getName());
        existing.setPhoneNumber(updateContact.getPhoneNumber());
        existing.setEmail(updateContact.getEmail());
        existing.setAddress(updateContact.getAddress());
        existing.setCompany(updateContact.getCompany());
        existing.setJobTitle(updateContact.getJobTitle());

        return contactRepository.save(existing);
    }

    // ✅ Delete contact (only if current user is owner)
    @Override
    public void deleteContact(Long contactId) {
        User currentUser = userService.getCurrentUser();
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Contact not found with ID: " + contactId));

        if (!contact.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized to delete this contact");
        }

        contactRepository.delete(contact);
    }

    // ✅ Get contact by ID (only if current user is owner)

    @Override
    public Contact getContactById(Long contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new GlobalExceptionHandler.ResourceNotFoundException("Contact not found with ID: " + contactId));

        User currentUser = userService.getCurrentUser();
        if (!contact.getUser().getId().equals(currentUser.getId())) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("You are not authorized to view this contact");
        }

        return contact;
    }


    // ✅ Get all contacts of current user
    @Override
    public List<Contact> getAllContactsOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return contactRepository.findByUser(currentUser);
    }
}
