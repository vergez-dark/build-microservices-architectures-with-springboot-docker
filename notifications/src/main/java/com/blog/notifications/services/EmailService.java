package com.blog.notifications.services;

import com.blog.notifications.entities.Users;
import com.blog.notifications.exceptions.GeneralException;
import com.blog.notifications.models.EmailMessage;

public interface EmailService {

    public void sendActivationEmail(Long userId, String activationHash) throws GeneralException;

    public void sendProjectRejectionEmail(Users user, String projectTitle)
            throws GeneralException;
    void publishEmailMessage(EmailMessage emailMessage);

}
