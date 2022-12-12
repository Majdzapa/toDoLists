package com.todolists.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.todolists.dtos.SubTaskDTO;
import com.todolists.dtos.ToDoDTO;
import com.todolists.exceptions.DataNotFoundException;
import com.todolists.models.SubTask;
import com.todolists.models.ToDo;
import com.todolists.repository.SubTaskRepository;
import com.todolists.repository.ToDoRepository;

@Service
public class SubTaskService {

	private ToDoRepository toDoRepository;
	private SubTaskRepository subTaskRepository;
	private ModelMapper modelMapper;

	@Autowired
	public SubTaskService(ToDoRepository toDoRepository,SubTaskRepository subTaskRepository,ModelMapper modelMapper) {
		this.toDoRepository = toDoRepository;
		this.subTaskRepository=subTaskRepository;
		this.modelMapper=modelMapper;
	}

	public List<SubTaskDTO> getAll() {
		return this.subTaskRepository.findAll().stream()
                .map(x -> modelMapper.map(x, SubTaskDTO.class)).collect(Collectors.toList());
	}

	public SubTaskDTO addSubTask(SubTaskDTO subTaskDTO) {
		    ToDo todo=this.toDoRepository.findById(subTaskDTO.getTodo().getId()).get();
			SubTask subTaskEntity= modelMapper.map(subTaskDTO, SubTask.class);
			subTaskEntity.setTodo(todo);
		    todo.addSubTask(subTaskEntity);
			SubTask subTask = subTaskRepository.save(subTaskEntity);
			return modelMapper.map(subTask, SubTaskDTO.class);
		
	}

	public void deleteSubTask(Integer id) {
		SubTask subTask = this.subTaskRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("SubTask with id: " + id + " is not found"));
		this.subTaskRepository.delete(subTask);

	}

	public SubTaskDTO updateSubTask(SubTaskDTO subTask ,Integer id) {

		return this.findById(id)
				.map(savedSubTask -> mappingForUpdate(subTask, savedSubTask))
				.map(this.subTaskRepository::save)
				.map(savedTask->modelMapper.map(savedTask, SubTaskDTO.class))
			    .orElseThrow(() -> new DataNotFoundException("SubTask with id: " + subTask.getId() + " is not found"));

	}

	private SubTask mappingForUpdate(SubTaskDTO task, SubTask savedTask) {
		savedTask.setDate(task.getDate());
		savedTask.setName(task.getName());
		savedTask.setDescription(task.getDescription());
		savedTask.setTodo(modelMapper.map(task.getTodo(), ToDo.class));
		return savedTask;
	}


	public Optional<SubTask> findById(Integer id) {
		ResponseEntity<SubTask> responseSubTask = new ResponseEntity<SubTask>(this.subTaskRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("subTask with id: " + id + " is not found")), HttpStatus.OK);
		return Optional.ofNullable(responseSubTask.getBody());
	}


	
}