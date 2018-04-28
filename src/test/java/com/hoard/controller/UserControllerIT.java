package com.hoard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private User user2 = new User("user1@email.com", "xxUserxx", "Scratch", "Phoenix");

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
    public void createNewUser() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(new ArrayList<User>());
        Mockito.when(userRepository.save((User) Mockito.any())).thenReturn(user1);
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(user1.getId())));
    }

    @Test
    public void createUserIDSupplied() throws Exception {
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(8)));
    }

    @Test
    public void createEmailInUse() throws Exception {
        List<User> userlist = new ArrayList<User>();
        userlist.add(user1);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(userlist);
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(8)));
    }

    @Test
    public void createUserNameInUse() {

    }

    @Test
    public void createBlankUserNameSetToEmail() {

    }

    @Test
    public void createUserInternalError() {

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

    /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}