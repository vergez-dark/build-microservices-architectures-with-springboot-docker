package com.example.comment_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private Long id;
    private String tk;
    private boolean revoked;
    private boolean expired;
}
