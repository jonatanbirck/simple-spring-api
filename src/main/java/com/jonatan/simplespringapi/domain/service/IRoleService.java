package com.jonatan.simplespringapi.domain.service;

import java.util.List;
import java.util.Optional;

import com.jonatan.simplespringapi.domain.entity.Role;

public interface IRoleService {
    
    Role saveRole(Role role);

    Optional<Role> getRole(Long id);

    Optional<Role> getRole(String name);

    List<Role> getRoles();

    Role updateRole(Role role);

    void deleteRole(Role role);

}
