// Exemple : Post.java
package com.example.comment_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Users {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String password;
    private Collection<AppRole> appRoles;

}
