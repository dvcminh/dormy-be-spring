package com.minhvu.apigateway.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataFromToken {

    private Long id;
    private String username;
    private String email;
}
