package com.jonatan.simplespringapi.domain.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.jonatan.simplespringapi.domain.entity.Role;
import com.jonatan.simplespringapi.domain.entity.User;
import com.jonatan.simplespringapi.domain.entity.UserState;
import com.jonatan.simplespringapi.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;  
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        
        return user.map( user_ -> {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            user_.getRoles().forEach( role -> { 
                authorities.add( new SimpleGrantedAuthority(role.getName()) ); 
            });

            return new org.springframework.security.core.userdetails.User( user_.getLogin(), user_.getPassword(), authorities );
        })
            .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
    }

    @Override
    public User saveUser(User user) {
        if( user.getRoles().isEmpty() ){
            List<Role> roles = new ArrayList();
            roles.add(roleService.getRole("USER").get());
            user.setRoles(roles);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setState(UserState.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUser(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        user.setState( UserState.DELETED );
    }

    @Override
    public void addRoleToUser(String userLogin, String roleName) {
        Optional<User> user = getUser( userLogin );

        if( user.isPresent() ) {
            Optional<Role> role = roleService.getRole(roleName);

            if( role.isPresent() ){
                user.get().getRoles().add(role.get());
            }
        }
    }

}
