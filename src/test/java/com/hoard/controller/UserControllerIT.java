package com.hoard.controller;

import com.hoard.entity.User;
import com.hoard.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    private MockMvc mockMvc;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserController userController;
    private User user1 = new User(1,"user1@email.com", "xxUserxx", "Scratch", "Phoenix");
    private String user1JSON = "{\"id\": 1,\"email\": \"user1@email.com\",\"userName\": \"xxUserxx\",\"firstName\": \"Scratch\"}";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @After
    public void deinit() {
        mockMvc = null;
    }

    @Test
    public void getNullUser() throws Exception {
        Mockito.when(
                userRepository.findOne(Mockito.anyInt())).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/get/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code", is(1)))
                .andExpect(jsonPath("detail", is("Item not found.")));
    }

    @Test
    public void getKnownUser() throws Exception {
        Mockito.when(
                userRepository.findOne(1)).thenReturn(user1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/get/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(user1.getId())))
                .andExpect(jsonPath("email", is(user1.getEmail())));
    }

    @Test
    public void createNewUser() {

    }

    @Test
    public void createExistingUser() {

    }

    @Test
    public void createUserIDSupplied() {

    }

    @Test
    public void createEmailInUse() {

    }

    @Test
    public void createUserNameInUse() {

    }

    @Test
    public void createBlankUserNameSetToEmail() {

    }

    @Test
    public void updateUser() {

    }

    @Test
    public void udpateUserIdMismatch() {

    }

    @Test
    public void updateInUseEmail() {

    }

    @Test
    public void updateNotFound() {

    }

    @Test
    public void updateNullUserName() {

    }

    @Test
    public void updateNoChange() {

    }

    @Test
    public void deleteUser() {

    }

    @Test
    public void deleteNotFound() {

    }

    @Test
    public void deleteUserIdMismatch() {

    }

    @Test
    public void deleteInternalError() {

    }
}