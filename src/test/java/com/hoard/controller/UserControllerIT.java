package com.hoard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoard.entity.User;
import com.hoard.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class UserControllerIT {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Configuration
    @EnableAutoConfiguration
    public static class Config {
        @Bean
        public UserController userController() {
            return new UserController();
        }
    }

    private User user = new User(1,"user@email.com", "xxUserxx", "Scratch", "Phoenix");
    private User userUpdate = new User(1, "user@email.com", "SuperFly", "Dank", "Memes");
    private User userNoId = new User("user@email.com", "xxUserxx", "Scratch", "Phoenix");
    private User userBlankUsername = new User("user@email.com", "", "Scratch", "Phoenix");
    private User userEmailUsername = new User(1, "user@email.com", "user@email.com", "Scratch", "Phoenix");
    private User userNoIdEmailUsername = new User("user@email.com", "user@email.com", "Scratch", "Phoenix");
    private User user2 = new User(2, "user2@email.com", "Nerdz", "Fire", "Fox");
    private User user2FailEmail = new User(2, "user@email.com", "Nerdz", "Fire", "Fox");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createNewUser() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(new ArrayList<User>());
        Mockito.when(userRepository.save((User) Mockito.any())).thenReturn(user);
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userNoId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(user.getId())));
    }

    @Test
    public void createUserIDSupplied() throws Exception {
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(8)));
    }

    @Test
    public void createEmailInUse() throws Exception {
        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(userlist);
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userNoId)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code", is(2)));
    }

    @Test
    public void createUserUsernameBlank() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(new ArrayList<User>());
        Mockito.when(userRepository.save(userNoIdEmailUsername)).thenReturn(userEmailUsername);
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userBlankUsername)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(user.getId())));
    }

    @Test
    public void createUserInternalError() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(null);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(new ArrayList<User>());
        Mockito.when(userRepository.save(userNoId)).thenReturn(null);
        mockMvc.perform(
                post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userNoId)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("code", is(9)));
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
                userRepository.findOne(1)).thenReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/get/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(user.getId())))
                .andExpect(jsonPath("email", is(user.getEmail())));
    }

    @Test
    public void updateUser() throws Exception {
        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user);
        Mockito.when(userRepository.findOne(1)).thenReturn(user);
        Mockito.when(userRepository.findByEmail(userUpdate.getEmail())).thenReturn(userlist);
        Mockito.when(userRepository.save(userUpdate)).thenReturn(userUpdate);
        mockMvc.perform(
          put("/api/user/update/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(asJsonString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("userName", is("SuperFly"))
        );
    }

    @Test
    public void updateUserIdMismatch() throws Exception {
        mockMvc.perform(
                put("/api/user/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(4))
        );
    }

    @Test
    public void updateInUseEmail() throws Exception {
        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user);
        Mockito.when(userRepository.findOne(2)).thenReturn(user2);
        Mockito.when(userRepository.findByEmail(userUpdate.getEmail())).thenReturn(userlist);
        mockMvc.perform(
                put("/api/user/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user2FailEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(2))
                );
    }

    @Test
    public void updateEmptyEmail() throws Exception {
        User userNoEmail = userUpdate;
        userNoEmail.setEmail("");
        ArrayList<User> userlist = new ArrayList<>();
        userlist.add(user);
        Mockito.when(userRepository.findOne(1)).thenReturn(user);
        Mockito.when(userRepository.findByEmail(userUpdate.getEmail())).thenReturn(userlist);
        Mockito.when(userRepository.save(userUpdate)).thenReturn(userUpdate);
        mockMvc.perform(
                put("/api/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userNoEmail)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(5))
                );
    }

    @Test
    public void updateNotFound() throws Exception {
        Mockito.when(userRepository.findOne(Mockito.anyInt())).thenReturn(null);
        mockMvc.perform(
                put("/api/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code", is(1)));
    }

    @Test
    public void updateNullUserName() throws Exception {
        User usernameNull = userUpdate;
        usernameNull.setUserName(null);
        mockMvc.perform(
                put("/api/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usernameNull)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateNoChange() throws Exception {
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(userRepository.findOne(1)).thenReturn(user);
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(userList);
        mockMvc.perform(
                put("/api/user/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user))
        ).andExpect(status().isOk()).andExpect(jsonPath("code", is(0)));
    }

    @Test
    public void deleteUser() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(user, user, null);
        mockMvc.perform(
                delete("/api/user/delete/1")).andExpect(status().isOk());
    }

    @Test
    public void deleteNotFound() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(null);
        mockMvc.perform(
                delete("/api/user/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code", is(1)));
    }

    @Test
    public void deleteInternalError() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(user);
        mockMvc.perform(
                delete("/api/user/delete/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("code", is(3)));
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