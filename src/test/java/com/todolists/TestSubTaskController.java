package com.todolists;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.todolists.config.WebSecurityConfig;
import com.todolists.controller.SubTaskController;
import com.todolists.controller.ToDoController;
import com.todolists.dtos.SubTaskDTO;
import com.todolists.dtos.ToDoDTO;
import com.todolists.models.SubTask;
import com.todolists.models.ToDo;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = SubTaskController.class,excludeAutoConfiguration = {WebSecurityConfig.class})

@TestMethodOrder(OrderAnnotation.class)

class TestSubTaskController {

	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SubTaskController subTaskController;
	ToDoDTO toDoDTO = new ToDoDTO(1, "test ToDo","description from test ToDo", LocalDate.of(2023,1,16), false,null);
	SubTaskDTO subTaskDTO = new SubTaskDTO(1, "subTask 1", toDoDTO, "description SubTask 1", LocalDate.of(2023,1,16));
	
			
	
	@Test
	@Order(1)
	@DisplayName("test Create subTaskDo Success")
	 void test_Create_SubTask_Success() throws Exception {
		Mockito.when(subTaskController.addTask(Mockito.any(SubTaskDTO.class)))
				.thenReturn(ResponseEntity.ok(subTaskDTO));
		
		String inputInJson = this.mapToJson(subTaskDTO);
		MvcResult results =mockMvc.perform(post("/subTask").accept(MediaType.APPLICATION_JSON).content(inputInJson).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	@Test
	@Order(2)
	@DisplayName("test Create SubTask Failed")
	 void test_Create_SubTask_Failed() throws Exception {
		SubTaskDTO wrongSubTaskDTO = new SubTaskDTO(1, "s", toDoDTO, "description SubTask 1", LocalDate.of(2023,1,16));
		String inputInJson = this.mapToJson(wrongSubTaskDTO);
		String URI = "/subTask";

		MvcResult results =mockMvc.perform(post(URI).accept(MediaType.APPLICATION_JSON).content(inputInJson).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	@Test
	@Order(3)
	@DisplayName("test GetAll SubTasks Success")
	public void test_GetAll_SubTask_Success() throws Exception {

		SubTaskDTO subTaskDTO1 = new SubTaskDTO(2, "subTask 2", toDoDTO, "description SubTask 2", LocalDate.of(2023,1,16));
		List<SubTaskDTO> subTaskList = new ArrayList<>();
		subTaskList.add(subTaskDTO);
		subTaskList.add(subTaskDTO1);
		Mockito.when(subTaskController.getAll()).thenReturn(subTaskList);
		MvcResult results =mockMvc.perform(get("/subTask").accept(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	@Test
	@Order(4)
	@DisplayName("test update SubTask Success")
	public void test_Update_SubTask_Success() throws Exception {
		String inputInJson = this.mapToJson(subTaskDTO);
		String URI = "/subTask/1/update";
		Mockito.when(subTaskController.update(Mockito.anyInt(),Mockito.any(SubTaskDTO.class))).thenReturn(new ResponseEntity<SubTaskDTO>(subTaskDTO,HttpStatus.OK));
		MvcResult results =mockMvc.perform(put(URI).accept(MediaType.APPLICATION_JSON).content(inputInJson).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());

	}

	@Test
	@Order(5)
	@DisplayName("test FindByID SubTask Success")
	public void test_GetSubTaskById_Success() throws Exception {
		ToDo toDo = new ToDo(1, "test ToDo","Description from test ToDo", LocalDate.of(2023,1,16), false,null);
		SubTask subTask=new SubTask(1, "subTask 1", toDo, "description SubTask 1", LocalDate.of(2023,1,16));
		
		Mockito.when(subTaskController.findById(Mockito.anyInt())).thenReturn(new ResponseEntity<SubTask>(subTask,HttpStatus.OK));
		String URI = "/subTask/1";
		MvcResult results =mockMvc.perform(get(URI).accept(MediaType.APPLICATION_JSON)).andReturn();
		Approvals.verify(results.getResponse().getContentAsString());
	}

	
	@Test
	@Order(6)
	@DisplayName("test delete SubTask Success")
	public void test_Delete_SubTask_Success() throws Exception {
		Mockito.when(subTaskController.deleteToDo(Mockito.anyInt())).thenReturn( new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED));
        String URI="/subTask/1/delete";
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

