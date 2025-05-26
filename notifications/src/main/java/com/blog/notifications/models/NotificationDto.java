package com.blog.notifications.models;

import com.blog.notifications.entities.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Data
public class NotificationDto {

    private String title;
    private String message;
    private Long recipientId;
    @Enumerated(EnumType.STRING)
    private NotificationType type;

}
