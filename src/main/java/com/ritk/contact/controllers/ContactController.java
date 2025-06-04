package com.ritk.contact.controllers;

import com.ritk.contact.entity.Contact;
import com.ritk.contact.payload.ApiResponse;
import com.ritk.contact.services.contact.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // ✅ New endpoint - Save contact WITH image
    @PostMapping(value = "/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Contact>> saveContactWithImage(
            @RequestParam("name") @NotBlank(message = "Name is required") String name,
            @RequestParam("phoneNumber") @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits") String phoneNumber,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "company", required = false) String company,
            @RequestParam(value = "jobTitle", required = false) String jobTitle,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest request
    ) {
        // Debug: Print headers to see if token is coming
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Authorization Header: " + authHeader);

        // Create contact object
        Contact contact = new Contact();
        contact.setName(name);
        contact.setPhoneNumber(phoneNumber);
        contact.setEmail(email);
        contact.setAddress(address);
        contact.setCompany(company);
        contact.setJobTitle(jobTitle);

        try {
            Contact saved = contactService.saveContactWithImage(contact, image);
            return ResponseEntity.ok(new ApiResponse<>("Contact with image saved successfully", true, saved));
        } catch (Exception e) {
            System.err.println("❌ Error saving contact: " + e.getMessage());
            throw e;
        }
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
