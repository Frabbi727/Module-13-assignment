package org.ostad._3_assignemnt.service;

import org.ostad._3_assignemnt.dto.CategoryCountDTO;
import org.ostad._3_assignemnt.dto.ContactDTO;
import org.ostad._3_assignemnt.dto.ContactProjection;
import org.ostad._3_assignemnt.entity.Contact;
import org.ostad._3_assignemnt.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    // ============= CRUD Operations =============

    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contact = convertToEntity(contactDTO);
        Contact savedContact = contactRepository.save(contact);
        return convertToDTO(savedContact);
    }

    public Optional<ContactDTO> getContactById(Long id) {
        return contactRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        contact.setFirstName(contactDTO.getFirstName());
        contact.setLastName(contactDTO.getLastName());
        contact.setPhoneNo(contactDTO.getPhoneNo());
        contact.setEmail(contactDTO.getEmail());
        contact.setCategory(contactDTO.getCategory());
        if (contactDTO.getIsActive() != null) {
            contact.setIsActive(contactDTO.getIsActive());
        }

        Contact updatedContact = contactRepository.save(contact);
        return convertToDTO(updatedContact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    // ============= Derived Query Methods =============

    public List<ContactDTO> getContactsByFirstName(String firstName) {
        return contactRepository.findByFirstName(firstName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactsByLastName(String lastName) {
        return contactRepository.findByLastName(lastName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ContactDTO> getContactByEmail(String email) {
        return contactRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    public List<ContactDTO> getContactsByCategory(String category) {
        return contactRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getActiveContacts() {
        return contactRepository.findByIsActive(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactsByCategoryAndStatus(String category, Boolean isActive) {
        return contactRepository.findByCategoryAndIsActive(category, isActive).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> searchContactsByName(String name) {
        return contactRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactsCreatedAfter(LocalDateTime date) {
        return contactRepository.findByCreationDateAfter(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactsCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return contactRepository.findByCreationDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getContactCountByCategory(String category) {
        return contactRepository.countByCategory(category);
    }

    public boolean emailExists(String email) {
        return contactRepository.existsByEmail(email);
    }

    // ============= Custom JPQL Query Methods =============

    public Page<ContactDTO> getActiveContactsByCategory(String category, Pageable pageable) {
        return contactRepository.findActiveContactsByCategory(category, pageable)
                .map(this::convertToDTO);
    }

    public Page<ContactDTO> searchByName(String name, Pageable pageable) {
        return contactRepository.searchByName(name, pageable)
                .map(this::convertToDTO);
    }

    public List<ContactDTO> getContactsByCategories(List<String> categories) {
        return contactRepository.findByCategories(categories).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public long getActiveContactCountByCategory(String category) {
        return contactRepository.countActiveContactsByCategory(category);
    }

    public List<ContactDTO> getRecentContacts(int days) {
        LocalDateTime date = LocalDateTime.now().minusDays(days);
        return contactRepository.findRecentContacts(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ContactProjection> getAllActiveProjections() {
        return contactRepository.findAllActiveProjections();
    }

    public List<ContactProjection> getContactProjectionsByCategory(String category) {
        return contactRepository.findByCategoryProjection(category);
    }

    public Page<ContactDTO> searchContacts(String firstName, String lastName, String category,
                                          Boolean isActive, Pageable pageable) {
        return contactRepository.searchContacts(firstName, lastName, category, isActive, pageable)
                .map(this::convertToDTO);
    }

    public List<CategoryCountDTO> getContactCountsByCategory() {
        List<Object[]> results = contactRepository.getContactCountByCategory();
        return results.stream()
                .map(result -> new CategoryCountDTO((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactsByEmailDomain(String domain) {
        return contactRepository.findByEmailDomain(domain).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ============= Helper Methods =============

    private ContactDTO convertToDTO(Contact contact) {
        ContactDTO dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setPhoneNo(contact.getPhoneNo());
        dto.setEmail(contact.getEmail());
        dto.setIsActive(contact.getIsActive());
        dto.setCategory(contact.getCategory());
        dto.setCreationDate(contact.getCreationDate());
        return dto;
    }

    private Contact convertToEntity(ContactDTO dto) {
        Contact contact = new Contact();
        if (dto.getId() != null) {
            contact.setId(dto.getId());
        }
        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setPhoneNo(dto.getPhoneNo());
        contact.setEmail(dto.getEmail());
        contact.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        contact.setCategory(dto.getCategory());
        return contact;
    }
}