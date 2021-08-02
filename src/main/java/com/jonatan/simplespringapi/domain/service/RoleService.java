package com.jonatan.simplespringapi.domain.service;

import java.util.List;
import java.util.Optional;

import com.jonatan.simplespringapi.domain.entity.Role;
import com.jonatan.simplespringapi.domain.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class RoleService implements IRoleService{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> getRole(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> getRole(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
    
}
