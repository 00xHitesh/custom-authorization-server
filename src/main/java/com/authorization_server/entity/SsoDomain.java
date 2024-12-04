package com.authorization_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "`sso_domain`", schema = "auth_server")
public class SsoDomain {

    @Getter
    @Id
    @Column(name = "oidc_registration_id", length = 200, nullable = false)
    private String oidcRegistrationId;

    @Column(length = 200, nullable = false)
    private String domainName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SsoDomain ssoDomain = (SsoDomain) o;
        return Objects.equals(oidcRegistrationId, ssoDomain.oidcRegistrationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oidcRegistrationId);
    }
}

