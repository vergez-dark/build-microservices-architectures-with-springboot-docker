package com.blog.authentications.clients;

import com.blog.authentications.request.MailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATIONS-SERVICE")
public interface NotificationServiceClient {

    @PostMapping("/api/email")
    void sendActivationEmail(@RequestBody MailRequest mailRequest);
}
