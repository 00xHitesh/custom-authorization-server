package com.authorization_server.entity;

import com.authorization_server.util.seq_gen.CustomSequenceGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(name = "authoritymaster", schema = "auth_server")
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_id_seq_gen")
    @GenericGenerator(name = "authority_id_seq_gen", type = CustomSequenceGenerator.class, parameters = {@org.hibernate.annotations.Parameter(name =
            "entity", value = "com.authorization_server.entity.Authority"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    protected Integer authorityId;

    private String name;

    public static Authority builder() {
        return new Authority();
    }

    public Authority id(Integer authorityId) {
        this.authorityId = authorityId;
        return this;
    }

    public Authority name(String name) {
        this.name = name;
        return this;
    }

    public Authority build() {
        return this;
    }

    public String getName() {
        return name;
    }
}