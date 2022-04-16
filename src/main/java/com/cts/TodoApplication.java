package com.cts;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cts.domain.Todo;
import com.cts.domain.TodoUser;
import com.cts.domain.TodoUserAuthority;
import com.cts.repo.TodoRepository;
import com.cts.repo.TodoUserRepository;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner init(TodoRepository repo, TodoUserRepository todoUserRepository, BCryptPasswordEncoder encoder) {
		return args -> {
			List<Todo> todos = Arrays.asList(Todo.builder()
					.description("Learning RestAPI")
					.created(LocalDateTime.now())
					.modified(LocalDateTime.now())
					.build(),
					Todo.builder()
							.description("Completed RestAPI")
							.created(LocalDateTime.now())
							.modified(LocalDateTime.now())
							.build(),
					Todo.builder()
							.description("Got a job")
							.created(LocalDateTime.now())
							.modified(LocalDateTime.now())
							.build());

			repo.saveAll(todos);

			TodoUser todoUser = TodoUser.builder()
					.username("admin")
					.password(encoder.encode("admin"))
					.email("n@a.com")
					.authorities(Arrays.asList(TodoUserAuthority.builder()
							.authority("ROLE_ADMIN")
							.build(),
							TodoUserAuthority.builder()
									.authority("ROLE_USER")
									.build()))
					.build();

			TodoUser todoUser2 = TodoUser.builder()
					.username("todo")
					.password(encoder.encode("todo"))
					.email("n@a.com")
					.authorities(Arrays.asList(TodoUserAuthority.builder()
							.authority("ROLE_USER")
							.build()))
					.build();

			todoUserRepository.save(todoUser);
			todoUserRepository.save(todoUser2);
		};
	}

}
