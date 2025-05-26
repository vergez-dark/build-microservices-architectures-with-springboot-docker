package com.blog.authentications.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class MailRequest {
    private Long userId;
    private String message;
}
