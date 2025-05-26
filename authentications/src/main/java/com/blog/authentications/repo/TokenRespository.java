package com.blog.authentications.repo;

import com.blog.authentications.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRespository extends JpaRepository<Token, Long> {

    Optional<Token> findByTk(String tk);
}
