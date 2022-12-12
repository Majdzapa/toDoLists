package com.todolists.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.todolists.dtos.ToDoDTO;
import com.todolists.exceptions.DataNotFoundException;
import com.todolists.models.ToDo;
import com.todolists.repository.ToDoRepository;

@Service
public class TodoService {

	private ToDoRepository toDoRepository;
	private ModelMapper modelMapper;

	@Autowired
	public TodoService(ToDoRepository toDoRepository,ModelMapper modelMapper) {
		this.toDoRepository = toDoRepository;
		this.modelMapper=modelMapper;
	}

	public List<ToDoDTO> getAll() {
		return this.toDoRepository.findAll().stream()
                .map(x -> modelMapper.map(x, ToDoDTO.class)).collect(Collectors.toList());
	}

	public ToDoDTO addTask(ToDoDTO toDoDTO) {
		ToDo todo = toDoRepository.save(modelMapper.map(toDoDTO, ToDo.class));
		return modelMapper.map(todo, ToDoDTO.class);
	}

	public void deleteToDo(Integer id) {
		ToDo task = this.toDoRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("ToDo with id: " + id + " is not found"));
		this.toDoRepository.delete(task);

	}

	public ToDoDTO updateToDo(ToDoDTO task ,Integer id) {

		return this.findById(id)
				.map(savedTask -> mappingForUpdate(task, savedTask))
				.map(this.toDoRepository::save)
				.map(savedTask->modelMapper.map(savedTask, ToDoDTO.class))
			    .orElseThrow(() -> new DataNotFoundException("ToDo with id: " + task.getId() + " is not found"));

	}

	private ToDo mappingForUpdate(ToDoDTO task, ToDo savedTask) {
		savedTask.setDate(task.getDate());
		savedTask.setIsDone(task.getIsDone());
		savedTask.setDescription(task.getDescription());
		savedTask.setTask(task.getTask());
		return savedTask;
	}

	
	
	
	public Optional<ToDo> findById(Integer id) {
		ResponseEntity<ToDo> responseToDo = new ResponseEntity<ToDo>(this.toDoRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("ToDo with id: " + id + " is not found")), HttpStatus.OK);
		return Optional.ofNullable(responseToDo.getBody());
	}


	
}
