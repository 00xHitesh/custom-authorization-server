package com.authorization_server.repository;

import com.authorization_server.entity.AuthorizationConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationConsentRepository extends JpaRepository<AuthorizationConsent,
        AuthorizationConsent.AuthorizationConsentId> {
    Optional<AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(String registeredClientId,
                                                                            String principalName);

    void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}