package com.minhvu.monolithic.dto;

import com.minhvu.monolithic.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private UUID id;


    @NotBlank(message = "Username is required")
    @Size(max = 50,message = "Username cannot be UUIDer than 50 characters")
    @Size(min = 3,message = "Username cannot be shorter than 3 characters")
    @Column(nullable = false,unique = true,length = 50)
    private  String userName;

    @Size(max = 255, message = "Bio cannot be more than 255 characters")
    @Column (length = 255)
    private String bio;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String fullName;
}
