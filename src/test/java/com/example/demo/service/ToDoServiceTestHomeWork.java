package com.example.demo.service;

import com.example.demo.dto.ToDoSaveRequest;
import com.example.demo.dto.mapper.ToDoEntityToResponseMapper;
import com.example.demo.exception.ToDoNotFoundException;
import com.example.demo.model.ToDoEntity;
import com.example.demo.repository.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ToDoServiceTestHomeWork {

    private ToDoRepository toDoRepository;

    private ToDoService toDoService;

    //executes before each test defined below
    @BeforeEach
    void setUp() {
        this.toDoRepository = mock(ToDoRepository.class);
        toDoService = new ToDoService(toDoRepository);
    }

    @Test
    void whenTextNotFound_thenThrowNotFoundException() {
        assertThrows(ToDoNotFoundException.class, () -> toDoService.getByText(null));
    }

    @Test
    void whenGetText_thenReturnCorrectText() throws ToDoNotFoundException {
        //mock
        var todo = new ToDoEntity(0L, "Test 1");
        when(toDoRepository.findFirstByTextEqualsIgnoreCase(anyString())).thenReturn(Optional.of(todo));

        //call
        var result = toDoService.getByText("Test 1");

        //validate
        assertThat(result, samePropertyValuesAs(
                ToDoEntityToResponseMapper.map(todo)
        ));
    }

    @Test
    void whenUpsertReceiveWrongId_ThenReturnThrows() {
        //when
        var todo = ToDoSaveRequest.builder()
                .text("test")
                .id(1000L)
                .build();
        //validate
        assertThrows(ToDoNotFoundException.class, () -> toDoService.upsert(todo));
    }

    @Test
    void whenCompleteToDoWrongId_thenRepositoryDeleteCalled() {
        //call
        var wrongId = 1000L;

        //validate
        assertThrows(ToDoNotFoundException.class, () -> toDoService.completeToDo(wrongId));
    }


}
