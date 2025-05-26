package com.blog.notifications.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class Users extends EntityBase {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String password;
    private Collection<AppRole> appRoles;
}
