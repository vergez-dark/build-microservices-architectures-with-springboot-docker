package com.blog.authentications.repo;

import com.blog.authentications.entities.ActivationHash;
import com.blog.authentications.entities.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ActivationHashRepository extends JpaRepository<ActivationHash, Long> {

    public ActivationHash findByHash(String hash);

    @Query("SELECT a FROM ActivationHash a WHERE a.id = ?1")
    public ActivationHash findByIdOrNull(Long id);

    @Modifying
    @Query("DELETE FROM ActivationHash a WHERE a.user = :user")
    void deleteByUser(@Param("user") Users user);
}
