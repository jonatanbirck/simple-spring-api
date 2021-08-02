package com.jonatan.simplespringapi.domain.service;

import java.util.List;
import java.util.Optional;

import com.jonatan.simplespringapi.domain.entity.User;

public interface IUserService {
    
    User saveUser(User user);

    Optional<User> getUser(Long id);

    Optional<User> getUser(String login);

    List<User> getUsers();

    User updateUser(User user);

    void deleteUser(User user);

    void addRoleToUser(String userLogin, String roleName);
}
