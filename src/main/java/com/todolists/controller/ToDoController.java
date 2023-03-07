package com.todolists.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.todolists.dtos.ToDoDTO;
import com.todolists.exceptions.DataNotFoundException;
import com.todolists.models.ToDo;
import com.todolists.services.TodoService;

@RestController
@RequestMapping(value = "/toDo")
@EnableWebMvc
public class ToDoController {


	private TodoService toDoService;
	
	
	@Autowired
	public ToDoController(TodoService toDoService) {
		this.toDoService=toDoService;
		
	}
	
	@GetMapping
	public List<ToDoDTO> getAll(){
		return this.toDoService.getAll();
	}
	
	@PostMapping
	public ResponseEntity<ToDoDTO> addTask(@RequestBody @Valid ToDoDTO toDoDTO) {
		return  ResponseEntity.ok(this.toDoService.addTask(toDoDTO));
	}
	

	@DeleteMapping(value="/{id}/delete",headers="Accept=*/*",produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity deleteToDo(@PathVariable("id") Integer id) throws DataNotFoundException {
		this.toDoService.deleteToDo(id);
		return ResponseEntity.accepted().build();
	}
	
	@PutMapping("/{id}/update")
	public ResponseEntity<ToDoDTO> update(@PathVariable("id") Integer id, @RequestBody ToDoDTO task) {
		ToDoDTO updatedToDo = this.toDoService.updateToDo(task,id);
		return ResponseEntity.ok(updatedToDo);
	}
	
	
	@GetMapping("/{id}")
	public  ResponseEntity<ToDo> findById(@PathVariable("id") Integer id) throws DataNotFoundException{
		return new ResponseEntity<ToDo>(this.toDoService.findById(id).orElseThrow(() ->new DataNotFoundException("ToDo with id: " + id + " is not found")) ,HttpStatus.OK);
	}

}
