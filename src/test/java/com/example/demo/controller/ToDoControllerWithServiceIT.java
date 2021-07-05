package com.example.demo.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Arrays;
import java.util.Collections;

import com.example.demo.model.ToDoEntity;
import com.example.demo.repository.ToDoRepository;
import com.example.demo.service.ToDoService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ToDoController.class)
@ActiveProfiles(profiles = "test")
@Import(ToDoService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

	//NEW TESTS

	@Test
	void whenGetAllCompleteNow_thenReturnValidResponse() throws Exception {
		String testText = "My to do text";

		when(toDoRepository.findByCompletedAtNotNull()).thenReturn(
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
				.andExpect(jsonPath("$[1].text").value(testText))
				.andExpect(jsonPath("$[2].text").value(testText))
				.andExpect(jsonPath("$[0].id").isNumber())
				.andExpect(jsonPath("$[1].id").isNumber())
				.andExpect(jsonPath("$[2].id").isNumber())
				.andExpect(jsonPath("$[0].completedAt").isNotEmpty())
				.andExpect(jsonPath("$[1].completedAt").isNotEmpty())
				.andExpect(jsonPath("$[2].completedAt").isNotEmpty());
	}


	@Test
	void whenNotGetCompleteNow_thenReturnEmptyValue() throws Exception {
		String testText = "My to do text";

		when(toDoRepository.findByCompletedAtNotNull()).thenReturn(
				Collections.singletonList(
						new ToDoEntity(1L, testText)
				)
		);

		this.mockMvc
				.perform(get("/todos/complete"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].text").value(testText))
				.andExpect(jsonPath("$[0].id").isNumber())
				.andExpect(jsonPath("$[0].completedAt").isEmpty());
	}

	@Test
	void whenGetAllButEntityEmpty_thenReturnValidResponse() throws Exception {
		//тут я хотів перевірити що буде
		//якщо немає ніяких задач
		//але не впевнений в правильності написаного
		//чи для того що я хотів написав тест, чи він провірить зовсім інше
		this.mockMvc
				.perform(get("/todos"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isEmpty())
				.andExpect(jsonPath("$", hasSize(0)))
				.andExpect(jsonPath("$[0].text").doesNotExist())
				.andExpect(jsonPath("$[0].id").doesNotExist())
				.andExpect(jsonPath("$[0].completedAt").doesNotExist());
	}

	@Test
	void whenGetWrongToDoId_thenReturnThrows() throws Exception {

		this.mockMvc
				.perform(get("/todos/1000"))
				.andExpect(status().isOk())
				.andExpect(content().string("Can not find todo with id 1000"));
	}

	@Test
	void whenPutCompleteWrongId_thenReturnThrows() throws Exception {
		this.mockMvc
				.perform(put("/todos/10/complete"))
				.andExpect(status().isOk())
				.andExpect(content().string("Can not find todo with id 10"));
	}

	@Test
	void whenDeleteOne_thenReturnValidResponse() throws Exception {
		this.mockMvc
				.perform(delete("/todos/{id}", 1L))
				.andExpect(status().isOk());
	}

	//Не дивлячись на на підсказку так і не зміг зрозуміти як заствити працювати ці тести
//	@Test
//	@Order(1)
//	public void whenSave_thenReturnValidResponse() throws Exception {
//
//		this.mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON)
//				.content("{\"id\": 3, \"text\": \"test text\"}")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$[2].id").value(3L))
//				.andExpect(jsonPath("$[2].text").value("test text"))
//				.andExpect(jsonPath("$[2].completedAt").doesNotExist());
//	}
//
//	@Test
//	@Order(2)
//	void whenToDoCompleted_thenReturnValidResponse() throws Exception {
//		this.mockMvc.perform(put("/todos/{id}/complete", 3L).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$[3].id").value(3L))
//				.andExpect(jsonPath("$[3].text").value("test text"))
//				.andExpect(jsonPath("$[3].completedAt").exists());
//	}
}
