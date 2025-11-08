package org.ostad._3_assignemnt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    private String phoneNo;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private Boolean isActive;

    @NotBlank(message = "Category is required")
    private String category;

    private LocalDateTime creationDate;
}