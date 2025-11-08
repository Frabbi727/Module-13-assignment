package org.ostad._3_assignemnt.repository;

import org.ostad._3_assignemnt.entity.Contact;
import org.ostad._3_assignemnt.dto.ContactProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // ============= Derived Query Methods =============

    // Find contacts by first name
    List<Contact> findByFirstName(String firstName);

    // Find contacts by last name
    List<Contact> findByLastName(String lastName);

    // Find contacts by email
    Optional<Contact> findByEmail(String email);

    // Find contacts by phone number
    Optional<Contact> findByPhoneNo(String phoneNo);

    // Find contacts by category
    List<Contact> findByCategory(String category);

    // Find contacts by active status
    List<Contact> findByIsActive(Boolean isActive);

    // Find contacts by category and active status
    List<Contact> findByCategoryAndIsActive(String category, Boolean isActive);

    // Find contacts by first name or last name (case insensitive)
    List<Contact> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    // Find contacts created after a specific date
    List<Contact> findByCreationDateAfter(LocalDateTime date);

    // Find contacts created between two dates
    List<Contact> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find contacts by category ordered by creation date descending
    List<Contact> findByCategoryOrderByCreationDateDesc(String category);

    // Count contacts by category
    long countByCategory(String category);

    // Check if contact exists by email
    boolean existsByEmail(String email);

    // Delete contacts by category
    void deleteByCategory(String category);

    // ============= Custom JPQL Queries =============

    // Find active contacts by category with pagination
    @Query("SELECT c FROM Contact c WHERE c.category = :category AND c.isActive = true")
    Page<Contact> findActiveContactsByCategory(@Param("category") String category, Pageable pageable);

    // Search contacts by name (first or last) with pagination
    @Query("SELECT c FROM Contact c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Contact> searchByName(@Param("name") String name, Pageable pageable);

    // Find contacts by multiple categories
    @Query("SELECT c FROM Contact c WHERE c.category IN :categories AND c.isActive = true")
    List<Contact> findByCategories(@Param("categories") List<String> categories);

    // Get contact count by category (JPQL)
    @Query("SELECT COUNT(c) FROM Contact c WHERE c.category = :category AND c.isActive = true")
    long countActiveContactsByCategory(@Param("category") String category);

    // Find contacts created in last N days
    @Query("SELECT c FROM Contact c WHERE c.creationDate >= :date ORDER BY c.creationDate DESC")
    List<Contact> findRecentContacts(@Param("date") LocalDateTime date);

    // Find contacts with projection (name and email only)
    @Query("SELECT c.firstName as firstName, c.lastName as lastName, c.email as email, " +
           "c.category as category FROM Contact c WHERE c.isActive = true")
    List<ContactProjection> findAllActiveProjections();

    // Find contacts by category with projection
    @Query("SELECT c.id as id, c.firstName as firstName, c.lastName as lastName, " +
           "c.phoneNo as phoneNo, c.email as email FROM Contact c WHERE c.category = :category")
    List<ContactProjection> findByCategoryProjection(@Param("category") String category);

    // Search contacts with multiple criteria
    @Query("SELECT c FROM Contact c WHERE " +
           "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:category IS NULL OR c.category = :category) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive)")
    Page<Contact> searchContacts(@Param("firstName") String firstName,
                                  @Param("lastName") String lastName,
                                  @Param("category") String category,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);

    // Get all categories with contact count
    @Query("SELECT c.category, COUNT(c) FROM Contact c GROUP BY c.category")
    List<Object[]> getContactCountByCategory();

    // Find contacts by email domain
    @Query("SELECT c FROM Contact c WHERE c.email LIKE CONCAT('%@', :domain)")
    List<Contact> findByEmailDomain(@Param("domain") String domain);

    // Update contact active status (native query example)
    @Query("SELECT c FROM Contact c WHERE c.id = :id")
    Optional<Contact> findContactById(@Param("id") Long id);
}