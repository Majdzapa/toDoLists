package com.todolists.dtos;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubTaskDTO {

	private Integer id;
	@NotEmpty(message = "name may not be empty")
	@Size(min = 2, max = 32, message = "name must be between 2 and 32 characters long")
	private String name;
	@JsonBackReference
	private ToDoDTO todo;
	private String description;
	private LocalDate date;


	
}