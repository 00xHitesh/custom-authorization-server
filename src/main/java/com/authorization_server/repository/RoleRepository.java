package com.authorization_server.repository;

import com.authorization_server.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>
{
    Optional<Role> findByName(String name);

    List<Role> findByRoleIdIn(List<Integer> ids);

}