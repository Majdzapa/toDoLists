package com.todolists.dtos;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class ToDoDTO  {


	private Integer id;

	@NotEmpty(message = "task may not be empty")
	@Size(min = 2, max = 32, message = "task must be between 2 and 32 characters long")
	private String task;

	private String description;
	
	private LocalDate date;

	private Boolean isDone;
	@JsonManagedReference
	private List<SubTaskDTO> subtaskSet = new ArrayList<>();
	public ToDoDTO(Integer id,
			@NotEmpty(message = "task may not be empty") @Size(min = 2, max = 32, message = "task must be between 2 and 32 characters long") String task,
			String description, LocalDate date, Boolean isDone, List<SubTaskDTO> subtaskSet) {
		super();
		this.id = id;
		this.task = task;
		this.description = description;
		this.date = date;
		this.isDone = isDone;
		this.subtaskSet = subtaskSet;
	}
	public ToDoDTO() {
		super();
	}

	
	
}