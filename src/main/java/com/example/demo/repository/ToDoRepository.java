package com.example.demo.repository;

import com.example.demo.model.ToDoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository extends JpaRepository<ToDoEntity, Long> {

    Optional<ToDoEntity> findFirstByTextEqualsIgnoreCase(String text);

    List<ToDoEntity> findByCompletedAtNotNull();
}