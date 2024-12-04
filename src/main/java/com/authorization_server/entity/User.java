package com.authorization_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.authorization_server.util.seq_gen.CustomSequenceGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "usermaster", schema = "auth_server")
public class User implements Serializable {

    @CreationTimestamp
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq_gen")
    @GenericGenerator(name = "user_id_seq_gen", type = CustomSequenceGenerator.class, parameters = {@org.hibernate.annotations.Parameter(name =
            "entity", value = "com.authorization_server.entity.User"), @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    private Integer userId;
    private String password;

    private String firstName;

    private String middleName;

    private String lastName;

    private String locale;

    private String avatarUrl;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_mapping", schema = "auth_server", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    private Integer active;
    @Column(name = "is_federated")
    private Integer federated;
    @Column(nullable = false, unique = true)
    private String email;
    private Integer emailVerified;
    @Column(nullable = false, unique = true)
    private String username;

    public static User builder() {
        return new User();
    }

    public User userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public User roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User locale(String locale) {
        this.locale = locale;
        return this;
    }

    public User avatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public User active(boolean active) {
        this.active = active ? 1 : 0;
        return this;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public User active(Integer active) {
        this.active = active;
        return this;
    }

    public User federated(boolean federated) {
        this.federated = federated ? 1 : 0;
        return this;
    }

    public User emailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified ? 1 : 0;
        return this;
    }

    public User build() {
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean getActive() {
        return active == 1;
    }

    public boolean getFederated() {
        return federated == 1;
    }

    public String getEmail() {
        return email;
    }

    public Integer getEmailVerified() {
        return emailVerified;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocale() {
        return locale;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}