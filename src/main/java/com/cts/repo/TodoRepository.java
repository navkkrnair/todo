package com.cts.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
