package com.hoard.controller;

import com.hoard.entity.User;
import com.hoard.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    private MockMvc mockMvc;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserController userController;
    User user1 = new User("user1@email.com", "xxUserxx", "Scratch", "Phoenix");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getNullUser() throws Exception {
        Mockito.when(
                userRepository.findOne(Mockito.anyInt())).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/user/get/1");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        System.out.println("My response: " + result.getResponse().getContentAsString());
        String expected = "{\"code\": 1,\"description\": \"Item not found.\"}";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }
}