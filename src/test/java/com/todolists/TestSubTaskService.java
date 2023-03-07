package com.todolists;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.approvaltests.Approvals;
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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.todolists.config.WebSecurityConfig;
import com.todolists.controller.SubTaskController;
import com.todolists.dtos.SubTaskDTO;
import com.todolists.dtos.ToDoDTO;
import com.todolists.models.SubTask;
import com.todolists.models.ToDo;
import com.todolists.services.SubTaskService;





@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = SubTaskService.class,excludeAutoConfiguration = {WebSecurityConfig.class})
@TestMethodOrder(OrderAnnotation.class)
class TestSubTaskService {

	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubTaskService subTaskService;
	ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","description from test ToDo", LocalDate.of(2023,1,16), false,null);
	SubTaskDTO subTaskDTO = new SubTaskDTO(1, "subTask 1", toDoDTO, "description SubTask 1", LocalDate.of(2023,1,16));
		
	@Test
	@Order(1)
	@DisplayName("test Create subTask Success")
	 void test_Service_Create_ToDo_Success() throws Exception {
		Mockito.when(subTaskService.addSubTask(Mockito.any(SubTaskDTO.class)))
				.thenReturn(subTaskDTO);
		SubTaskDTO created=subTaskService.addSubTask(subTaskDTO);
		Approvals.verify(mapToJson(created));
	}

	@Test
	@Order(2)
	@DisplayName("test Create subTask Failed")
	void test_Service_Create_ToDo_Failed() throws Exception {

		SubTaskDTO wrongSubTaskDTO = new SubTaskDTO(1, "x", toDoDTO, "xx", LocalDate.of(2023,1,16));
		Mockito.when(subTaskService.addSubTask(Mockito.any(SubTaskDTO.class))).thenReturn(wrongSubTaskDTO);
		SubTaskDTO created=subTaskService.addSubTask(wrongSubTaskDTO);
		Approvals.verify(mapToJson(created));
	}

	@Test
	@Order(3)
	@DisplayName("test GetAll subTask Success")
	public void test_Service_GetAll_ToDo_Success() throws Exception {
		SubTaskDTO subTaskDTO1 = new SubTaskDTO(1, "subTask 1", toDoDTO, "description SubTask 1", LocalDate.of(2023,1,16));
			
		List<SubTaskDTO> subTaskList = new ArrayList<>();
		subTaskList.add(subTaskDTO);
		subTaskList.add(subTaskDTO1);
		Mockito.when(subTaskService.getAll()).thenReturn(subTaskList);
		List<SubTaskDTO> result = subTaskService.getAll();
		Approvals.verify(mapToJson(result));
	}

	@Test
	@Order(4)
	@DisplayName("test update subTask Success")
	public void test_Service_Update_ToDo_Success() throws Exception {
		Mockito.when(subTaskService.updateSubTask(Mockito.any(SubTaskDTO.class),Mockito.anyInt())).thenReturn(subTaskDTO);
		
		SubTaskDTO result=subTaskService.updateSubTask(subTaskDTO,subTaskDTO.getId());
		Approvals.verify(mapToJson(result));

	}

	@Test
	@Order(5)
	@DisplayName("test FindByID subTask Success")
	public void test_Service_GetToDoById_Success() throws Exception {
		ToDo toDo = new ToDo(1, "test ToDo","description from test ToDo",LocalDate.of(2023,1,16), false,null);
		SubTask subTask = new SubTask(1, "subTask 1", toDo, "description SubTask 1",LocalDate.of(2023,1,16));
		Mockito.when(subTaskService.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(subTask));
		Optional<SubTask> result=subTaskService.findById(subTask.getId());
		
		Approvals.verify(mapToJson(result.get()));
	}

	
//	@Test
//	@Order(6)
//	@DisplayName("test delete ToDo Success")
//	public void test_Delete_whengivenToDo_Success() throws Exception {
//		Mockito.when(todoService.deleteToDo(Mockito.anyInt())).thenReturn();
//		Approvals.verify(new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED));
//	}


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
