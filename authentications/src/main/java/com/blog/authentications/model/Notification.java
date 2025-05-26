package com.blog.authentications.model;

import com.blog.authentications.entities.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor @Builder @Data
public class Notification extends EntityBase {

    private String title;
    private String message;
    private Users recipient;
    private boolean read = false;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
