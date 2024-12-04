package com.authorization_server.repository;

import com.authorization_server.entity.OidcClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OidcClientRepository extends JpaRepository<OidcClient, String> {

    Optional<OidcClient> findByRegistrationId(String registrationId);

}
