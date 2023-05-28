package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.entity.AuthServer;
import com.github.ynovice.felicita.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select c.id.user from OAuth2Credential c where c.id.externalId = ?1 and c.id.authServer = ?2")
    Optional<User> findByExternalIdAndAuthServer(String externalId, AuthServer authServer);

    boolean existsByUsername(String username);
}
