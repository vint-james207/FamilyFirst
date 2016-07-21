package com.james;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.james.entities.Task;
import com.james.entities.User;
import com.james.services.TaskRepository;
import com.james.services.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FamilyFirstApplication.class)
@WebAppConfiguration
public class FamilyFirstApplicationTests {

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@Autowired
	UserRepository users;

	@Autowired
	TaskRepository tasks;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void aTestAdd() throws Exception {
		long oldcount = users.count();
		User user = new User("alice", "password");
		users.save(user);

		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(user);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/login")
						.content(json)
						.contentType("application/json")
		);

		Assert.assertTrue(users.count() == oldcount + 1);
	}

	@Test
	public void addTaskTest() throws Exception {
		User testuser = new User("bob", "pass");
		users.save(testuser);
		Task task = new Task(testuser, "thing to do", null, null, false);
		tasks.save(task);

		ResultActions ra = mockMvc.perform(
				MockMvcRequestBuilders.get("/tasks")
		);

		Assert.assertTrue(tasks.findOne((int) tasks.count()).getTaskText().contains("thing"));
	}

//	@Test
//	public void TasksGetRouteTest() throws Exception {
//		User testuser = new User("bob", "pass");
//		users.save(testuser);
//		Task task = new Task(testuser, "thing to do", null, null, false);
//		tasks.save(task);
//
//		ResultActions ra = mockMvc.perform(
//				MockMvcRequestBuilders.get("/tasks")
//		);
//		MvcResult result = ra.andReturn();
//		MockHttpServletResponse response = result.getResponse();
//		String json = response.getContentAsString();
//
//		ObjectMapper om = new ObjectMapper();
//		ArrayList<Task> testList = om.readValue(json, ArrayList.class);
//
//		System.out.println(testList.get(0).getTaskText());
//	}

}
