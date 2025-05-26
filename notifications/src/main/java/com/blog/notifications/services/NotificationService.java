package com.blog.notifications.services;

import com.blog.notifications.entities.Notification;
import com.blog.notifications.models.NotificationDto;

import java.util.List;

public interface NotificationService {
    public Notification createAndSendNotification(NotificationDto notification);
    public List<Notification> getUserNotifications(Long userId);
    public Notification markAsRead(Long notificationId);
    public List<Notification> getUnreadNotifications(Long recipientId);
    public List<Notification> sendUnreadNotificationsToUser(Long recipientId);
}
