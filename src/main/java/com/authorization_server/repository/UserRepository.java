package com.authorization_server.repository;

import com.authorization_server.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>
{
    Optional<User> findByEmailAndActive(String email, Integer active);

    Optional<User> findById(Integer id);

    Optional<User> findByUserIdAndActiveAndFederated(Integer userId, Integer active, Integer isFederated);
    Optional<User> findByUserIdAndActive(Integer userId, Integer active);
    void deleteById(Integer id);

    Optional<User> findByEmail(String email);
}