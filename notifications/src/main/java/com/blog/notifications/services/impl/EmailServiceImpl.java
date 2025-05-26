package com.blog.notifications.services.impl;

import com.blog.notifications.client.UserServiceClient;
import com.blog.notifications.entities.Users;
import com.blog.notifications.exceptions.GeneralException;
import com.blog.notifications.models.EmailMessage;
import com.blog.notifications.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String FRONTEND_BASE_URL = "frontendBaseUrl";
    private static final String FRONTEND_HOME_URL = "frontendHomeUrl";
    private static final String UTF_8 = "UTF-8";
    private static final String LOGO_PATH = "images/logo.png";
    private static final String SITE_URL = "siteUrl";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final RabbitTemplate rabbitTemplate;
    private final UserServiceClient userServiceClient;

    @Value("${app.rabbitmq.exchange.email}")
    private String emailExchange;

    @Value("${app.rabbitmq.routing-key.email}")
    private String emailRoutingKey;

    @Value("${app.frontend_base_url}")
    private String frontendBaseUrl;

    @Value("${app.account.frontend_home_url}")
    private String frontendHomeUrl;

    @Value("${app.account.url_activation_prefix}")
    private String frontendAccountUrlActivationPrefix;

    @Value("${test.mail.username}")
    private String senderEmail;

    @Value("${app.mail.sender_name}")
    private String senderName;

    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine,
                            RabbitTemplate rabbitTemplate,
                            UserServiceClient userServiceClient) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.rabbitTemplate = rabbitTemplate;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void sendActivationEmail(Long userId, String activationHash) throws GeneralException {
        EmailMessage message = new EmailMessage(userId, null, activationHash, null, "ACTIVATION");
        publishEmailMessage(message);
    }

    @Override
    public void sendProjectRejectionEmail(Users user, String projectTitle) throws GeneralException {
        EmailMessage message = new EmailMessage(null, user.getEmail(), null, projectTitle, "REJECTION");
        publishEmailMessage(message);
    }

    @Override
    public void publishEmailMessage(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, emailMessage);
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.email}")
    public void consumeEmailMessage(EmailMessage message) {
        try {
            if ("ACTIVATION".equals(message.getType())) {
                sendActivationEmailInternal(message.getUserId(), message.getActivationHash());
            } else if ("REJECTION".equals(message.getType())) {
//                Users user = userRepository.findById(message.getUserId())
//                        .orElseThrow(() -> new GeneralException("User not found"));
                Users user = userServiceClient.findUsersById(message.getUserId());
                sendProjectRejectionEmailInternal(user, message.getProjectTitle());
            }
        } catch (GeneralException e) {
            // Gérer les erreurs (par exemple, journaliser ou réessayer)
            throw new RuntimeException("Failed to process email message: " + e.getMessage(), e);
        }
    }

    private void sendActivationEmailInternal(Long userId, String activationHash) throws GeneralException {
        try {
            Users user = userServiceClient.findUsersById(userId);
            if (user == null) {
                throw new GeneralException("User not found");
            }
            System.out.println(user.toString());
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8);
            if (senderEmail != null && senderName != null) {
                helper.setFrom(senderEmail, senderName);
            }

            helper.setTo(user.getEmail());
            helper.setSubject("Activation de compte");

            Context context = new Context();
            context.setVariable("accountActivationUrl", frontendBaseUrl
                    + frontendAccountUrlActivationPrefix + "?hash=" + activationHash);
            context.setVariable("name", user.getFirstName() + ' ' + user.getLastName());
            context.setVariable(SITE_URL, frontendBaseUrl + frontendHomeUrl);
            String emailContent = templateEngine.process("account_activation_template", context);
            if (emailContent != null) {
                helper.setText(emailContent, true);
            }

            helper.addInline("logo", new ClassPathResource(LOGO_PATH));

            mailSender.send(message);
        } catch (MailException | UnsupportedEncodingException | MessagingException e) {
            throw new GeneralException(e.getMessage());
        }
    }

    private void sendProjectRejectionEmailInternal(Users user, String projectTitle) throws GeneralException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8);

            if (senderEmail != null && senderName != null) {
                helper.setFrom(senderEmail, senderName);
            }

            helper.setTo(user.getEmail());
            helper.setSubject("Rejet de votre projet");

            Context context = new Context();
            context.setVariable("userName", user.getFirstName() + ' ' + user.getLastName());
            context.setVariable("projectTitle", projectTitle);

            String emailContent = templateEngine.process("project_rejection_template", context);
            if (emailContent != null) {
                helper.setText(emailContent, true);
            }

            // Décommentez si nécessaire
            // helper.addInline("logo", new ClassPathResource(LOGO_PATH));

            mailSender.send(message);
        } catch (MailException | UnsupportedEncodingException | MessagingException e) {
            throw new GeneralException(e.getMessage());
        }
    }
}
