package com.todolists;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.approvaltests.Approvals;
import org.approvaltests.JsonApprovals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.todolists.dtos.ToDoDTO;
import com.todolists.models.SubTask;
import com.todolists.models.ToDo;
import com.todolists.services.TodoService;



@WebMvcTest(value = TodoService.class)
@TestMethodOrder(OrderAnnotation.class)
class TestToDoService {

	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TodoService todoService;

	ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","Description from test ToDo", LocalDate.now(), false,null);
	
	@Test
	@Order(1)
	@DisplayName("test Create ToDo Success")
	 void test_Service_Create_ToDo_Success() throws Exception {
		Mockito.when(todoService.addTask(Mockito.any(ToDoDTO.class)))
				.thenReturn(toDoDTO);
		ToDoDTO result=todoService.addTask(toDoDTO);
		Approvals.verify(mapToJson(result));
	}

	@Test
	@Order(2)
	@DisplayName("test Create ToDo Failed")
	void test_Service_Create_ToDo_Failed() throws Exception {

		ToDoDTO wrong_toDo = new ToDoDTO(1, "t", "Description from test ToDo", LocalDate.now(), false, null);
		Mockito.when(todoService.addTask(Mockito.any(ToDoDTO.class))).thenReturn(wrong_toDo);
		ToDoDTO result=todoService.addTask(wrong_toDo);
		Approvals.verify(mapToJson(result));
	}

	@Test
	@Order(3)
	@DisplayName("test GetAll ToDo Success")
	public void test_Service_GetAll_ToDo_Success() throws Exception {
		ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","Description from test ToDo", LocalDate.now(), false,null);
		ToDoDTO toDoDTO1 = new ToDoDTO(1, "test ToDo","Description from test ToDo", LocalDate.now(), false,null);
		List<ToDoDTO> toDoList = new ArrayList<>();
		toDoList.add(toDoDTO);
		toDoList.add(toDoDTO1);
		Mockito.when(todoService.getAll()).thenReturn(toDoList);
		List<ToDoDTO> result=todoService.getAll();
		Approvals.verify(mapToJson(result));
	}

	@Test
	@Order(4)
	@DisplayName("test update ToDo Success")
	public void test_Service_Update_ToDo_Success() throws Exception {
		ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","Description from test ToDo", LocalDate.now(), false,null);
		Mockito.when(todoService.updateToDo(Mockito.any(ToDoDTO.class),Mockito.anyInt())).thenReturn(toDoDTO);
		ToDoDTO result= todoService.updateToDo(toDoDTO,toDoDTO.getId());
		Approvals.verify(mapToJson(result));

	}

	@Test
	@Order(5)
	@DisplayName("test FindByID ToDo Success")
	public void test_Service_GetToDoById_Success() throws Exception {
		ToDo toDo = new ToDo(1, "test ToDo","Description from test ToDo", LocalDate.now(), false,null);
		Mockito.when(todoService.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(toDo));
		Optional<ToDo> result=todoService.findById(toDo.getId());
		Approvals.verify(mapToJson(result.get()));
	}



	/**
	 * Method used to map an Object into a JSON String
	 */
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return objectMapper.writeValueAsString(object);
	}

}
