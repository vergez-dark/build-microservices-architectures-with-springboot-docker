package com.blog.authentications.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Activation Hash is mandatory")
    private String activationHash;
}
