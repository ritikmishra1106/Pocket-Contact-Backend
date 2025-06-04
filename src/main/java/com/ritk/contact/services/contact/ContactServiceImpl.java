package com.ritk.contact.services.contact;

import com.ritk.contact.entity.Contact;
import com.ritk.contact.entity.User;
import com.ritk.contact.exception.GlobalExceptionHandler;
import com.ritk.contact.repository.ContactRepository;
import com.ritk.contact.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserService userService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;


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

    @Override
    public Contact saveContactWithImage(Contact contact, MultipartFile image) {
        User currentUser = userService.getCurrentUser();

        // Duplicate check
        Optional<Contact> existing = contactRepository.findByUserAndPhoneNumber(currentUser, contact.getPhoneNumber());
        if (existing.isPresent()) {
            throw new RuntimeException("Contact already exists with this phone number.");
        }

        if (image != null && !image.isEmpty()) {
            try {
                // ✅ Static upload path (defined in application.properties)
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath); // Create folder if doesn't exist
                }

                String originalFilename = image.getOriginalFilename();
                String fileExtension = "";

                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String fileName = UUID.randomUUID().toString() + fileExtension;

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // ✅ Save only relative path
                contact.setProfileImage("uploads/" + fileName); // frontend will prepend base URL

                System.out.println("✅ Image uploaded: " + filePath.toAbsolutePath());

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
            }
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
