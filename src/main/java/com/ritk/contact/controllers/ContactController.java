package com.ritk.contact.controllers;

import com.ritk.contact.entity.Contact;
import com.ritk.contact.services.contact.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    //  Save contact with userId
    @PostMapping("/user/{userId}")
    public ResponseEntity<Contact> saveContact(
            @PathVariable Long userId,
            @Valid @RequestBody Contact contact
    ) {
        Contact savedContact = contactService.saveContactWithUser(contact, userId);
        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
    }

    // ✅ Update contact
    @PutMapping("/{contactId}")
    public ResponseEntity<Contact> updateContact(
            @PathVariable Long contactId,
            @Valid @RequestBody Contact updateContact
    ) {
        Contact existing = contactService.getContactById(contactId);

        existing.setName(updateContact.getName());
        existing.setPhoneNumber(updateContact.getPhoneNumber());
        existing.setEmail(updateContact.getEmail());
        existing.setAddress(updateContact.getAddress());
        existing.setCompany(updateContact.getCompany());
        existing.setJobTitle(updateContact.getJobTitle());

        Contact updated = contactService.updateContact(existing);
        return ResponseEntity.ok(updated);
    }

    //  Delete contact
    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        contactService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }

    //  Get contact by ID
    @GetMapping("/{contactId}")
    public ResponseEntity<Contact> getContact(@PathVariable Long contactId) {
        Contact contact = contactService.getContactById(contactId);
        return ResponseEntity.ok(contact);
    }

    //  Get all contacts of user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Contact>> getAllContactOfUser(@PathVariable Long userId) {
        List<Contact> contacts = contactService.getAllContactsOfUser(userId);
        return ResponseEntity.ok(contacts);
    }
}
