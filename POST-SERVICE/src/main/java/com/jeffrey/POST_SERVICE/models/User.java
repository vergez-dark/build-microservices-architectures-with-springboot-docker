package com.jeffrey.POST_SERVICE.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class User  extends BaseModel {
    
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String password;
    private Collection<AppRole> appRoles;
}
