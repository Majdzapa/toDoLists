package com.todolists.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.todolists.dtos.SubTaskDTO;
import com.todolists.dtos.ToDoDTO;
import com.todolists.exceptions.DataNotFoundException;
import com.todolists.models.SubTask;
import com.todolists.models.ToDo;
import com.todolists.services.TodoService;
import com.todolists.services.SubTaskService;

@RestController
@RequestMapping(value = "/subTask")
public class SubTaskController {


	private TodoService toDoService;
	private SubTaskService subTaskService;
	private ModelMapper modelMapper;
	
	@Autowired
	public SubTaskController(TodoService toDoService,SubTaskService subTaskService, ModelMapper modelMapper) {
		this.toDoService=toDoService;
		this.subTaskService=subTaskService;
		this.modelMapper=modelMapper;
	}
	
	@GetMapping
	public List<SubTaskDTO> getAll(){
		return this.subTaskService.getAll();
	}
	
	@PostMapping
	public ResponseEntity<SubTaskDTO> addTask(@RequestBody @Valid SubTaskDTO subTaskDTO) {
		return  ResponseEntity.ok(this.subTaskService.addSubTask(subTaskDTO));
	}
	

	@DeleteMapping("/{id}/delete")
	@ResponseBody
	public ResponseEntity deleteToDo(@PathVariable("id") Integer id) throws DataNotFoundException {
		this.subTaskService.deleteSubTask(id);
		return ResponseEntity.accepted().build();
	}
	
	@PutMapping("/{id}/update")
	public ResponseEntity<SubTaskDTO> update(@PathVariable("id") Integer id, @RequestBody SubTaskDTO task) {
		SubTaskDTO updatedToDo = this.subTaskService.updateSubTask(task,id);
		return ResponseEntity.ok(updatedToDo);
	}
	
	
	@GetMapping("/{id}")
	public  ResponseEntity<SubTask> findById(@PathVariable("id") Integer id) throws DataNotFoundException{
		return new ResponseEntity<SubTask>(this.subTaskService.findById(id).orElseThrow(() ->new DataNotFoundException("SubTask with id: " + id + " is not found")) ,HttpStatus.OK);
	}
}