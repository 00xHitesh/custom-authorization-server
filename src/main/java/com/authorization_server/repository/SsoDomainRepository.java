package com.authorization_server.repository;

import com.authorization_server.entity.SsoDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SsoDomainRepository extends JpaRepository<SsoDomain, String> {

    Optional<SsoDomain> findByOidcRegistrationId(String registrationId);

}
