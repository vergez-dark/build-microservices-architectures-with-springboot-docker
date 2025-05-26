package com.blog.notifications.repositories;

import com.blog.notifications.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long userId);
    List<Notification> findByRecipientIdAndRead(Long userId, boolean read);
    List<Notification> findByRecipientIdIsNullAndReadIsFalse();
    long countByRecipientIdAndRead(Long userId, boolean read);
    List<Notification> findByRecipientIdOrderByCreatedOnDesc(Long userId);
}
