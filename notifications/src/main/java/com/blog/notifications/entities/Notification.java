package com.blog.notifications.entities;

import com.blog.notifications.models.NotificationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity @AllArgsConstructor @NoArgsConstructor @Builder @Data
public class Notification extends EntityBase {

    private String title;
    private String message;
    private Long recipientId;
    @Transient
    private Users recipient;
    private boolean read = false;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
