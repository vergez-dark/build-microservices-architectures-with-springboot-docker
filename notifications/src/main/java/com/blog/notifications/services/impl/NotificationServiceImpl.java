package com.blog.notifications.services.impl;

import com.blog.notifications.client.UserServiceClient;
import com.blog.notifications.entities.Notification;
import com.blog.notifications.entities.Users;
import com.blog.notifications.models.NotificationDto;
import com.blog.notifications.repositories.NotificationRepository;
import com.blog.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserServiceClient userServiceClient;

    @Override
    public Notification createAndSendNotification(NotificationDto notification) {
        Users user;
        if (notification.getRecipientId() != null) {
          user = userServiceClient.findUsersById(notification.getRecipientId());
        }else {
            user = null;
        }
        Notification notificationToSend = new Notification();
        notificationToSend.setTitle(notification.getTitle());
        notificationToSend.setMessage(notification.getMessage());
        notificationToSend.setType(notification.getType());
        notificationToSend.setRecipient(user);
        Notification savedNotification = notificationRepository.save(notificationToSend);
        // Sauvegarde en base
        if (user == null) {
            messagingTemplate.convertAndSend("/topic/notification",
                    savedNotification
            );
            return savedNotification;
        }
        messagingTemplate.convertAndSendToUser(notification.getRecipientId().toString(),"/notification",
                savedNotification
        );
        return savedNotification;
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedOnDesc(userId);
    }

    @Override
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long recipientId) {
        List<Notification> userNotifications = notificationRepository.findByRecipientIdAndRead(recipientId, false);
        List<Notification> globalNotifications = notificationRepository.findByRecipientIdIsNullAndReadIsFalse();
        userNotifications.addAll(globalNotifications);
        return userNotifications;
    }

    // Récupérer et envoyer les notifications non lues à un utilisateur spécifique
    @Override
    public List<Notification> sendUnreadNotificationsToUser(Long recipientId) {
        List<Notification> unreadNotifications = getUnreadNotifications(recipientId);
        List<Notification> notifications = notificationRepository.findAll();
        int unreadNotificationsSize = unreadNotifications.size();
        messagingTemplate.convertAndSendToUser(recipientId.toString(),"/notification/unread",
                unreadNotifications
        );

        return unreadNotifications;
    }

    // Envoyer les notifications non lues à tous les utilisateurs connectés
//    private void sendUnreadNotificationsToAllConnectedUsers() {
//        // Utiliser SimpUserRegistry pour obtenir les utilisateurs connectés
//        simpUserRegistry.getUsers().forEach(user -> {
//            String recipient = user.getName();
//            sendUnreadNotificationsToUser(recipient);
//        });
//    }
//
//    @Scheduled(fixedRate = 60000)
//    public void deleteOldNotifications() {
//        System.out.println("deleting old notifications------------------------------------------------crone start");
//        notificationRepository.deleteAllInBatch();
//        System.out.println("deleting old notifications------------------------------------------------crone end sucessfully");
//    }
}
