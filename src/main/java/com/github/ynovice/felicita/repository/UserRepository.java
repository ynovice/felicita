package com.github.ynovice.felicita.repository;

import com.github.ynovice.felicita.model.AuthServer;
import com.github.ynovice.felicita.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select case when (count(c) > 0) then true else false end" +
            " from OAuth2Credential c where c.id.externalId = ?1 and c.id.authServer = ?2")
    Boolean existsByExternalIdAndAuthServer(String externalId, AuthServer authServer);

    @Query("select c.id.user from OAuth2Credential c where c.id.externalId = ?1 and c.id.authServer = ?2")
    Optional<User> findByExternalIdAndAuthServer(String externalId, AuthServer authServer);
}
