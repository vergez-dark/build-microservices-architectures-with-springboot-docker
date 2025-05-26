package com.blog.notifications.controllers;

import com.blog.notifications.entities.Users;
import com.blog.notifications.exceptions.GeneralException;
import com.blog.notifications.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    @PostMapping
    public void sendActivationEmail(@RequestBody MailRequest mailRequest) throws GeneralException {
        emailService.sendActivationEmail(mailRequest.getUserId(), mailRequest.getMessage());
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MailRequest{
    private Long userId;
    private String message;
}
