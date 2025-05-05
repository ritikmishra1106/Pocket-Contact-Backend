package com.ritk.contact.services.contact;

import com.ritk.contact.entity.Contact;

import java.util.List;

public interface ContactService {
    Contact saveContactWithUser(Contact contact, Long userId);

    Contact updateContact(Contact contact);
    void deleteContact(Long id);
    Contact getContactById(Long id);

    List<Contact> getAllContactsOfUser(Long userId);

}
