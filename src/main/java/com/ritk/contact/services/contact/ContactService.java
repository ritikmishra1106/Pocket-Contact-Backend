package com.ritk.contact.services.contact;

import com.ritk.contact.entity.Contact;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContactService {

    Contact saveContactForCurrentUser(Contact contact);

    Contact saveContactWithImage(Contact contact, MultipartFile image);

    Contact updateContact(Long contactId, Contact contact);

    void deleteContact(Long contactId);

    Contact getContactById(Long contactId);

    List<Contact> getAllContactsOfCurrentUser();
}
