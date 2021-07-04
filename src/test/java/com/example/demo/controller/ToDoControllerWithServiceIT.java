package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.demo.model.ToDoEntity;
import com.example.demo.repository.ToDoRepository;
import com.example.demo.service.ToDoService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ToDoController.class)
@ActiveProfiles(profiles = "test")
@Import(ToDoService.class)
class ToDoControllerWithServiceIT {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ToDoRepository toDoRepository;

	@Test
	void whenGetAll_thenReturnValidResponse() throws Exception {
		String testText = "My to do text";
		when(toDoRepository.findAll()).thenReturn(
				Collections.singletonList(
						new ToDoEntity(1L, testText)
				)
		);
		
		this.mockMvc
			.perform(get("/todos"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].text").value(testText))
			.andExpect(jsonPath("$[0].id").isNumber())
			.andExpect(jsonPath("$[0].completedAt").doesNotExist());
	}

	@Test
	void whenGetAllCompleteNow_thenReturnValidResponse() throws Exception {
		String testText = "My to do text";

		when(toDoRepository.findByCompletedAndNotNull()).thenReturn(
				Arrays.asList(
						new ToDoEntity(1L, testText).completeNow(),
						new ToDoEntity(2L, testText).completeNow(),
						new ToDoEntity(3L, testText).completeNow()
				)
		);

		this.mockMvc
				.perform(get("/todos/complete"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].text").value(testText))
				.andExpect(jsonPath("$[0].id").isNumber())
				.andExpect(jsonPath("$[0].completedAt").doesNotExist())

				.andExpect(jsonPath("$[1].text").value(testText))
				.andExpect(jsonPath("$[1].id").isNumber())
				.andExpect(jsonPath("$[1].completedAt").doesNotExist())

				.andExpect(jsonPath("$[2].text").value(testText))
				.andExpect(jsonPath("$[2].id").isNumber())
				.andExpect(jsonPath("$[2].completedAt").doesNotExist());
	}

}
