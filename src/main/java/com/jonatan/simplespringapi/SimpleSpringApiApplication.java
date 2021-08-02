package com.jonatan.simplespringapi;

import java.util.ArrayList;

import com.jonatan.simplespringapi.domain.entity.Role;
import com.jonatan.simplespringapi.domain.entity.User;
import com.jonatan.simplespringapi.domain.entity.UserState;
import com.jonatan.simplespringapi.domain.service.RoleService;
import com.jonatan.simplespringapi.domain.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SimpleSpringApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleSpringApiApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CommandLineRunner run(UserService userService, RoleService roleService) {
		
		return args -> {
			roleService.saveRole(new Role(null,"SUPER_USER"));
			roleService.saveRole(new Role(null,"ADMIN"));
			roleService.saveRole(new Role(null,"MANAGER"));
			roleService.saveRole(new Role(null,"USER"));

			userService.saveUser(new User(null,"Super Usuário","sudo","sudo123",new ArrayList<>(),UserState.ACTIVE));
			userService.addRoleToUser("sudo", "SUPER_USER");
		};
	}
}
