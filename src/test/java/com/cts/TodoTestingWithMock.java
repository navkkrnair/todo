package com.cts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.domain.Todo;
import com.cts.repo.TodoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TodoTestingWithMock {

	@Autowired
	MockMvc mvc;

	@MockBean
	TodoRepository todoRepository;
	Todo todo;

	@BeforeAll
	public void setup() {
		todo = Todo.builder()
				.id(10L)
				.description("Testing RestAPI")
				.created(LocalDateTime.now())
				.modified(LocalDateTime.now())
				.build();
		given(todoRepository.findById(1234L)).willReturn(Optional.of(todo));

	}

	@Test
	void testTodoWithMock() throws Exception {

		mvc.perform(get("/api/todo/1234"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.description", is(equalTo("Testing RestAPI"))));
	}

}
