package com.jonatan.simplespringapi.domain.repository;

import java.util.Optional;

import com.jonatan.simplespringapi.domain.entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long>{
    
    Optional<Role> findByName(String name);

}
