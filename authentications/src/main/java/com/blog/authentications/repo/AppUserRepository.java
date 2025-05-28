package com.blog.authentications.repo;

import com.blog.authentications.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    List<Users> findByIsActiveFalseAndCreatedOnBefore(Date dateTime);
}
