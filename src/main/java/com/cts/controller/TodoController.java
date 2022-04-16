package com.cts.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.cts.domain.Todo;
import com.cts.error.NoEntityFoundException;
import com.cts.error.TodoError;
import com.cts.repo.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
public class TodoController {

	private final TodoRepository todoRepository;

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Todo>> getTodos() {
		log.info("Getting all Todo's");
		return ResponseEntity.ok(todoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getTodoById(@PathVariable Long id) {
		log.info("Getting a Todo by id {}", id);
		Optional<Todo> optionalTodo = todoRepository.findById(id);
		return optionalTodo.isPresent() ? ResponseEntity.ok(optionalTodo.get())
				: ResponseEntity.badRequest()
						.body(TodoError.builder()
								.message("No Todo found!")
								.build());
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<?> setCompleted(@PathVariable Long id) {
		log.info("Updating a Todo with id {}", id);
		Optional<Todo> optionalTodo = todoRepository.findById(id);
		if (optionalTodo.isEmpty()) {
			/*
			 * return ResponseEntity.badRequest() .body(TodoError.builder()
			 * .message("No Todo found") .build());
			 */
			throw new NoEntityFoundException("Entity with id " + id + " not found");
		}

		Todo todo = optionalTodo.get();
		todo.setCompleted(true);
		todo.setModified(LocalDateTime.now());
		Todo savedTodo = todoRepository.save(todo);
		// http://localhost:8080/api/todo/4
		UriComponents location = ServletUriComponentsBuilder.fromCurrentRequest()
				.buildAndExpand(savedTodo.getId());
		return ResponseEntity.ok()
				.header("Location", location.toString())
				.build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method = { RequestMethod.POST,
			RequestMethod.PUT }, consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> createTodo(@RequestBody @Valid Todo todo, Errors errors) {
		if (errors.hasErrors()) {
			log.info("Validation errors found");
			List<String> errList = new ArrayList<>();
			errors.getFieldErrors()
					.forEach(oe -> errList.add(oe.getField() + ": " + oe.getDefaultMessage()));
			log.info("Sending an error response");
			return ResponseEntity.badRequest()
					.body(TodoError.builder()
							.message("Validation failed")
							.errors(errList)
							.build());
		}

		log.info("Saving a new {}", todo);
		Todo savedTodo = todoRepository.save(todo);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedTodo.getId())
				.toUri();
		return ResponseEntity.created(uri)
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
		// todoRepository.deleteById(id);
		log.info("Deleting Todo with id {}", id);

		Todo deleteTodo = Todo.builder()
				.id(id)
				.build();
		todoRepository.delete(deleteTodo);
		return ResponseEntity.noContent()
				.build();
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public TodoError handleException(Exception exception) {
		return TodoError.builder()
				.message(exception.getMessage())
				.build();
	}

}
