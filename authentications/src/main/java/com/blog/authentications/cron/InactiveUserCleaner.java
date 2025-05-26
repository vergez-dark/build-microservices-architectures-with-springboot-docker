package com.blog.authentications.cron;


import com.blog.authentications.entities.Users;
import com.blog.authentications.repo.ActivationHashRepository;
import com.blog.authentications.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class InactiveUserCleaner {

    private final AppUserRepository appUserRepository;

    private final ActivationHashRepository activationHashRepository;

    // Cron: toutes les 5 minutes (ajustable)
    @Transactional
    @Scheduled(cron = "0 */5 * * * *") // Toutes les 5 minutes
    public void cleanInactiveUsers() {
        Date thirtyMinutesAgo = new Date(System.currentTimeMillis() - (5 * 60 * 1000)); // 30 minutes en millisecondes
        List<Users> inactiveUsers = appUserRepository.findByIsActiveFalseAndCreatedOnBefore(thirtyMinutesAgo);
        System.out.println(thirtyMinutesAgo);
        if (!inactiveUsers.isEmpty()) {
            for (Users user : inactiveUsers) {
                activationHashRepository.deleteByUser(user);
            }
            log.info("Suppression de {} utilisateurs inactifs", inactiveUsers.size());
            appUserRepository.deleteAll(inactiveUsers);
        } else {
            log.info("Aucun utilisateur inactif à supprimer.");
        }
    }

}
