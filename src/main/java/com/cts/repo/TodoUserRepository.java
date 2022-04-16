package com.cts.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.domain.TodoUser;

public interface TodoUserRepository extends JpaRepository<TodoUser, Long> {
	TodoUser findByUsername(String username);

}
