package com.todolists.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "toDo")

public class ToDo {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotEmpty(message = "task may not be empty")
	@Size(min = 2, max = 32, message = "task must be between 2 and 32 characters long")
	private String task;

	private String description;
	
	private LocalDate date;

	private Boolean isDone;

	@JoinColumn
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JsonManagedReference
	private List<SubTask> subtaskSet= new ArrayList();
	
	
	public ToDo addSubTask(SubTask subTask) {
		subtaskSet.add(subTask);
		subTask.setTodo(this);
        return this;
    }

	
}
