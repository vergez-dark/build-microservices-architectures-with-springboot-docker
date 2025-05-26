package com.blog.notifications.handlers;

import com.blog.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        // Récupérer le userId des attributs de la session
        String userIdS = (String) sessionAttributes.get("userId");
        if (userIdS != null) {
            Long userId = Long.parseLong(userIdS);
            // Envoyer les notifications non lues à l'utilisateur connecté
            System.out.println("yo voici l'id de l'utilisateur-------------------------------------------------------------------------------------------"+userId);
            notificationService.sendUnreadNotificationsToUser(userId);
        }
    }
}
