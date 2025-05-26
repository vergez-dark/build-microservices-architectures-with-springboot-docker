package com.blog.authentications.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class ActivationHashGenerator {

    ActivationHashGenerator() {
    }

    public static String generateActivationHash() {
        try {
            // Génération d'un UUID unique
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            // Calcul du hash SHA-256 à partir de l'UUID
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(uuidString.getBytes(StandardCharsets.UTF_8));

            // Conversion du tableau de bytes en une représentation hexadécimale
            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hashBuilder.append('0');
                }
                hashBuilder.append(hex);
            }

            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
