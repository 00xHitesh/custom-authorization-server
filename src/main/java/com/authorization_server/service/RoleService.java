package com.authorization_server.service;

import com.authorization_server.entity.Role;

public interface RoleService {
    Role getByName(String name);
    Role getDefaultRole();
}
