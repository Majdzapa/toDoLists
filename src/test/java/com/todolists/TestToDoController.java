package com.todolists;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.approvaltests.Approvals;
import org.approvaltests.core.ApprovalTestPackageSettings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.todolists.config.WebSecurityConfig;
import com.todolists.controller.ToDoController;
import com.todolists.dtos.ToDoDTO;
import com.todolists.models.ToDo;



@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = ToDoController.class,excludeAutoConfiguration = {WebSecurityConfig.class})

class TestToDoController {

	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ToDoController toDoController;

	ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","Description from test ToDo", LocalDate.of(2023,1,16), false,null);
	
	@Test
	@Order(1)
	@DisplayName("test Create ToDo Success")
	 void testCreateToDo_Success() throws Exception {
		Mockito.when(toDoController.addTask(Mockito.any(ToDoDTO.class)))
				.thenReturn(ResponseEntity.ok(toDoDTO));
		
		String inputInJson = this.mapToJson(toDoDTO);
		MvcResult results =mockMvc.perform(post("/toDo").accept(MediaType.APPLICATION_JSON).content(inputInJson).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	@Test
	@Order(2)
	@DisplayName("test Create ToDo Failed")
	 void testCreateToDo_Failed() throws Exception {
	
		ToDoDTO wrong_toDo = new ToDoDTO(1, "t","Description from test ToDo", LocalDate.of(2023,1,16), false,null);
		String inputInJson = this.mapToJson(wrong_toDo);
		String URI = "/toDo";

		MvcResult results =mockMvc.perform(post(URI).accept(MediaType.APPLICATION_JSON).content(inputInJson).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	@Test
	@Order(3)
	@DisplayName("test GetAll ToDo Success")
	public void givenList_testGetAllToDo_Success() throws Exception {
		ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","Description from test ToDo",LocalDate.of(2023,1,16), false,null);
		ToDoDTO toDoDTO1 = new ToDoDTO(1, "test ToDo","Description from test ToDo", LocalDate.of(2023,1,16), false,null);
		List<ToDoDTO> toDoList = new ArrayList<>();
		toDoList.add(toDoDTO);
		toDoList.add(toDoDTO1);
		Mockito.when(toDoController.getAll()).thenReturn(toDoList);
		MvcResult results =mockMvc.perform(get("/toDo/").accept(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	@Test
	@Order(4)
	@DisplayName("test update ToDo Success")
	public void testUpdateToDo_Success() throws Exception {
		ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","Description from test ToDo",LocalDate.of(2023,1,16), false,null);
		String inputInJson = this.mapToJson(toDoDTO);
		String URI = "/toDo/1/update";
		Mockito.when(toDoController.update(Mockito.anyInt(),Mockito.any(ToDoDTO.class))).thenReturn(new ResponseEntity<ToDoDTO>(toDoDTO,HttpStatus.OK));
		MvcResult results =mockMvc.perform(put(URI).accept(MediaType.APPLICATION_JSON).content(inputInJson).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());

	}

	@Test
	@Order(5)
	@DisplayName("test FindByID ToDo Success")
	public void testGetToDoById_Success() throws Exception {
		ToDo toDo = new ToDo(1, "test ToDo","Description from test ToDo", LocalDate.of(2023,1,16), false,null);
		Mockito.when(toDoController.findById(Mockito.anyInt())).thenReturn(new ResponseEntity<ToDo>(toDo,HttpStatus.OK));
		String URI = "/toDo/1";
		MvcResult results =mockMvc.perform(get(URI).accept(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	
	@Test
	@Order(6)
	@DisplayName("test delete ToDo Success")
	public void test_Delete_whengivenToDo_Success() throws Exception {
		Mockito.when(toDoController.deleteToDo(Mockito.anyInt())).thenReturn( new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED));
        String URI="/toDo/1/delete";
		MvcResult results =mockMvc.perform(delete(URI).accept(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getStatus());
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
