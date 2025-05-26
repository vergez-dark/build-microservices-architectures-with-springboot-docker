package com.blog.authentications;

import com.blog.authentications.entities.AppRole;
import com.blog.authentications.entities.Users;
import com.blog.authentications.repo.AppRoleRepository;
import com.blog.authentications.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
public class AuthenticationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationsApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(AccountService accountService, AppRoleRepository roleRepository) {
		return args -> {
			if (!roleRepository.findByRoleName("USER").isPresent()) {
				accountService.addNewRole(new AppRole(null, "USER"));
			}
			if (!roleRepository.findByRoleName("ADMIN").isPresent()){
				accountService.addNewRole(new AppRole(null, "ADMIN"));
			}

//			accountService.addRoleToUser("user1", "USER");
//			accountService.addRoleToUser("user1", "SELLER");
//			accountService.addRoleToUser("admin", "USER");
//			accountService.addRoleToUser("admin", "SELLER");
//			accountService.addRoleToUser("admin", "ADMIN");
		};
	}
}
