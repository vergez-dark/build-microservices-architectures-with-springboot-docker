package com.blog.authentications.entities;

import com.blog.authentications.model.Comment;
import com.blog.authentications.model.EntityBase;
import com.blog.authentications.model.Notification;
import com.blog.authentications.model.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users extends EntityBase {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;

    //utiliser pour definir que cet attributs de la classe ne sera accessible que en ecriture
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> appRoles = new ArrayList<>();
    @Transient
    private List<Post> posts;
    @Transient
    private List<Comment> comments;
    @Transient
    private List<Notification> notifications;
}
