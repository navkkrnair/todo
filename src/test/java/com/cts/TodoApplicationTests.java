package com.cts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.domain.Todo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// End - to - End
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class TodoApplicationTests {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	@Order(2)
	void getAllTodos() throws Exception {
		mvc.perform(get("/api/todo").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@Order(1)
	void postATodo() throws JsonProcessingException, Exception {
		Todo todo = Todo.builder()
				.description("Listen to music")
				.created(LocalDateTime.now())
				.modified(LocalDateTime.now())
				.build();
		mvc.perform(post("/api/todo").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(todo)))
				.andExpect(status().isCreated());

	}

	@Test
	@Order(3)
	void getASingleTodo() throws Exception {
		mvc.perform(get("/api/todo/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.description", is(equalTo("Learning RestAPI"))))
				.andExpect(jsonPath("$.completed", is(false)));
	}

	@Test
	@Order(4)
	void getANonExistentTodo() throws Exception {
		mvc.perform(get("/api/todo/10").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is(equalTo("No Todo found!"))));
	}

	@Test
	@Order(5)
	void patchATodo() throws Exception {
		mvc.perform(patch("/api/todo/1"))
				.andExpect(status().isOk())
				.andExpect(header().exists("Location"));
	}

}
