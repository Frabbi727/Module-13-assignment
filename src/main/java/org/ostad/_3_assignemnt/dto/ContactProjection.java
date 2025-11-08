package org.ostad._3_assignemnt.dto;

/**
 * Projection interface for Contact entity
 * Used to retrieve only specific fields instead of the entire entity
 */
public interface ContactProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getPhoneNo();
    String getEmail();
    String getCategory();
}