package com.minhvu.authservice.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInformationRequest {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String avatar;
}