package org.ostad._3_assignemnt.controller;

import jakarta.validation.Valid;
import org.ostad._3_assignemnt.dto.CategoryCountDTO;
import org.ostad._3_assignemnt.dto.ContactDTO;
import org.ostad._3_assignemnt.dto.ContactProjection;
import org.ostad._3_assignemnt.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // ============= CRUD Endpoints =============

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO createdContact = contactService.createContact(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        return contactService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts() {
        List<ContactDTO> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@PathVariable Long id,
                                                    @Valid @RequestBody ContactDTO contactDTO) {
        ContactDTO updatedContact = contactService.updateContact(id, contactDTO);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    // ============= Derived Query Endpoints =============

    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<List<ContactDTO>> getContactsByFirstName(@PathVariable String firstName) {
        List<ContactDTO> contacts = contactService.getContactsByFirstName(firstName);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<ContactDTO>> getContactsByLastName(@PathVariable String lastName) {
        List<ContactDTO> contacts = contactService.getContactsByLastName(lastName);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ContactDTO> getContactByEmail(@PathVariable String email) {
        return contactService.getContactByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ContactDTO>> getContactsByCategory(@PathVariable String category) {
        List<ContactDTO> contacts = contactService.getContactsByCategory(category);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ContactDTO>> getActiveContacts() {
        List<ContactDTO> contacts = contactService.getActiveContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/category/{category}/status/{isActive}")
    public ResponseEntity<List<ContactDTO>> getContactsByCategoryAndStatus(
            @PathVariable String category,
            @PathVariable Boolean isActive) {
        List<ContactDTO> contacts = contactService.getContactsByCategoryAndStatus(category, isActive);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ContactDTO>> searchContactsByName(@RequestParam String name) {
        List<ContactDTO> contacts = contactService.searchContactsByName(name);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/created-after")
    public ResponseEntity<List<ContactDTO>> getContactsCreatedAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<ContactDTO> contacts = contactService.getContactsCreatedAfter(date);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/created-between")
    public ResponseEntity<List<ContactDTO>> getContactsCreatedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ContactDTO> contacts = contactService.getContactsCreatedBetween(startDate, endDate);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/count/category/{category}")
    public ResponseEntity<Long> getContactCountByCategory(@PathVariable String category) {
        long count = contactService.getContactCountByCategory(category);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = contactService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    // ============= Custom JPQL Query Endpoints with Pagination =============

    @GetMapping("/category/{category}/active/paginated")
    public ResponseEntity<Page<ContactDTO>> getActiveContactsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ContactDTO> contacts = contactService.getActiveContactsByCategory(category, pageable);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/search/name/paginated")
    public ResponseEntity<Page<ContactDTO>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContactDTO> contacts = contactService.searchByName(name, pageable);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ContactDTO>> getContactsByCategories(@RequestParam List<String> categories) {
        List<ContactDTO> contacts = contactService.getContactsByCategories(categories);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/count/category/{category}/active")
    public ResponseEntity<Long> getActiveContactCountByCategory(@PathVariable String category) {
        long count = contactService.getActiveContactCountByCategory(category);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ContactDTO>> getRecentContacts(@RequestParam(defaultValue = "7") int days) {
        List<ContactDTO> contacts = contactService.getRecentContacts(days);
        return ResponseEntity.ok(contacts);
    }

    // ============= Projection Endpoints =============

    @GetMapping("/projections/active")
    public ResponseEntity<List<ContactProjection>> getAllActiveProjections() {
        List<ContactProjection> projections = contactService.getAllActiveProjections();
        return ResponseEntity.ok(projections);
    }

    @GetMapping("/projections/category/{category}")
    public ResponseEntity<List<ContactProjection>> getContactProjectionsByCategory(@PathVariable String category) {
        List<ContactProjection> projections = contactService.getContactProjectionsByCategory(category);
        return ResponseEntity.ok(projections);
    }

    // ============= Advanced Search Endpoint =============

    @GetMapping("/search/advanced")
    public ResponseEntity<Page<ContactDTO>> searchContacts(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<ContactDTO> contacts = contactService.searchContacts(firstName, lastName, category, isActive, pageable);
        return ResponseEntity.ok(contacts);
    }

    // ============= Statistics Endpoints =============

    @GetMapping("/statistics/category-counts")
    public ResponseEntity<List<CategoryCountDTO>> getContactCountsByCategory() {
        List<CategoryCountDTO> counts = contactService.getContactCountsByCategory();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/email-domain/{domain}")
    public ResponseEntity<List<ContactDTO>> getContactsByEmailDomain(@PathVariable String domain) {
        List<ContactDTO> contacts = contactService.getContactsByEmailDomain(domain);
        return ResponseEntity.ok(contacts);
    }
}