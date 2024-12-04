package com.authorization_server.service;

import com.authorization_server.entity.Role;
import com.authorization_server.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    public static final String DEFAULT_ROLE = "FORECAST_USER";

    private final RoleRepository repository;

    @Override
    public Role getByName(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return repository.findByName(name).orElse(null);
    }

    @Override
    public Role getDefaultRole() {
        return getByName(DEFAULT_ROLE);
    }
}