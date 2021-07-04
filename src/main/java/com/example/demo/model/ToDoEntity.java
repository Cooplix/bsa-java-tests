package com.example.demo.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * ToDoEntity
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoEntity {

	@Id
	@NotNull
	@GeneratedValue
	private Long id;

	@Basic
	@NotNull
	private String text;

	@Basic
	private ZonedDateTime completedAt;

	public ToDoEntity(String text) {
		this.text = text;
	}
	
	public ToDoEntity(Long id, String text) {
		this.id = id;
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format(
			"ToDoEntity[id=%d, text='%s', completedAt='%s']",
			id, text, completedAt.toString()
		);
	}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public ToDoEntity setText(String text) {
		this.text = text;
		return this;
	}

	public ZonedDateTime getCompletedAt() {
		return completedAt;
	}
	public ToDoEntity completeNow() {
		completedAt = ZonedDateTime.now(ZoneOffset.UTC);
		return this;
	}
}