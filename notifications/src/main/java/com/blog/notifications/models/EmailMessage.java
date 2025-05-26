package com.blog.notifications.models;

import java.io.Serializable;

public class EmailMessage implements Serializable {
    private Long userId;
    private String email;
    private String activationHash;
    private String projectTitle;
    private String type; // "ACTIVATION" ou "REJECTION"

    // Constructeurs, getters et setters
    public EmailMessage() {}

    public EmailMessage(Long userId, String email, String activationHash, String projectTitle, String type) {
        this.userId = userId;
        this.email = email;
        this.activationHash = activationHash;
        this.projectTitle = projectTitle;
        this.type = type;
    }

    // Getters et setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationHash() {
        return activationHash;
    }

    public void setActivationHash(String activationHash) {
        this.activationHash = activationHash;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
