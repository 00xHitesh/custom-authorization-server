package com.authorization_server.entity;

import com.authorization_server.util.seq_gen.CustomSequenceGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "rolemaster", schema = "auth_server")
@Entity
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq_gen")
    @GenericGenerator(name = "role_id_seq_gen", type = CustomSequenceGenerator.class, parameters =
            {@org.hibernate.annotations.Parameter(name = "entity", value = "com.authorization_server.entity.Role"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    protected Integer roleId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_authority_mapping", schema = "auth_server", joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities = new HashSet<>();

    private String name;

    public static Role builder() {
        return new Role();
    }

    public Role id(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public Role authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public Role name(String name) {
        this.name = name;
        return this;
    }

    public Role build() {
        return this;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public String getName() {
        return name;
    }

    public Integer getRoleId() {
        return roleId;
    }
}