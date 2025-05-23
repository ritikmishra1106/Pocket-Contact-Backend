package com.ritk.contact.services.contact;

import com.ritk.contact.entity.Contact;

import java.util.List;

public interface ContactService {

    Contact saveContactForCurrentUser(Contact contact);

    Contact updateContact(Long contactId, Contact contact);

    void deleteContact(Long contactId);

    Contact getContactById(Long contactId);

    List<Contact> getAllContactsOfCurrentUser();
}
