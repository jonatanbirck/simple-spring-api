package com.jonatan.simplespringapi.domain.repository;

import java.util.Optional;

import com.jonatan.simplespringapi.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByLogin(String login);
    
    Optional<User> findByName(String name);

}
