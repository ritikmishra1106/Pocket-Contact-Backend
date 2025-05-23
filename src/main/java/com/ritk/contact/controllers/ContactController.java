package com.ritk.contact.controllers;

import com.ritk.contact.entity.Contact;
import com.ritk.contact.payload.ApiResponse;
import com.ritk.contact.services.contact.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // ✅ Save contact for current user

    @PostMapping
    public ResponseEntity<ApiResponse<Contact>> saveContact(@Valid @RequestBody Contact contact) {
        Contact saved = contactService.saveContactForCurrentUser(contact);
        ApiResponse<Contact> response = new ApiResponse<>("Contact added successfully", true, saved);
        return ResponseEntity.ok(response);
    }

    // ✅ Update contact
    @PutMapping("/{contactId}")
    public ResponseEntity<ApiResponse<Contact>> updateContact(
            @PathVariable Long contactId,
            @Valid @RequestBody Contact contact
    ) {
        Contact updated = contactService.updateContact(contactId, contact);
        ApiResponse<Contact> response = new ApiResponse<>("Contact updated successfully", true, updated);
        return ResponseEntity.ok(response);
    }


    // ✅ Delete contact
    @DeleteMapping("/{contactId}")
    public ResponseEntity<ApiResponse<String>> deleteContact(@PathVariable Long contactId) {
        contactService.deleteContact(contactId);
        ApiResponse<String> response = new ApiResponse<>("Contact deleted successfully", true, null);
        return ResponseEntity.ok(response);
    }

    // ✅ Get contact by ID
    @GetMapping("/{contactId}")
    public ResponseEntity<ApiResponse<Contact>> getContactById(@PathVariable Long contactId) {
        Contact contact = contactService.getContactById(contactId);
        ApiResponse<Contact> response = new ApiResponse<>("Contact fetched successfully", true, contact);
        return ResponseEntity.ok(response);
    }


    // ✅ Get all contacts of current user
    @GetMapping
    public ResponseEntity<ApiResponse<List<Contact>>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContactsOfCurrentUser();
        ApiResponse<List<Contact>> response = new ApiResponse<>("Contacts fetched successfully", true, contacts);
        return ResponseEntity.ok(response);
    }

}
