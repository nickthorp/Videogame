package com.hoard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoard.entity.User;
import com.hoard.entity.Videogame;
import com.hoard.repository.UserRepository;
import com.hoard.repository.VideogameRepository;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class VideogameControllerUT {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private VideogameRepository videogameRepository;
    @MockBean
    private UserRepository userRepository;

    private User user = new User(1,"user@email.com", "xxUserxx", "Scratch", "Phoenix");
    private Videogame vg = new Videogame(1, user, "Super Mario", "Nintendogs", "SNES", false, false, true);
    private Videogame vgNoID = new Videogame(user, "Super Mario", "Nintendogs", "SNES", false, false, true);

    @Configuration
    @EnableAutoConfiguration
    public static class Config {
        @Bean
        public VideogameController videogameController(){ return new VideogameController(); }
    }

    /*
     * converts a Java object into JSON representation
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createVideogame() throws Exception {
        Mockito.when(userRepository.findOne(1)).thenReturn(user);
        Mockito.when(videogameRepository.save(vgNoID)).thenReturn(vg);
        mockMvc.perform(
                post("/api/videogame/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vgNoID))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(vg.getId())));
    }

    @Test
    public void createVideogameNoUserID() {

    }

    @Test
    public void createVideogameUserNotFound() {

    }

    @Test
    public void createVideogameExtraID() {

    }

    @Test
    public void createVideogameNullJSON() {

    }

    @Test
    public void createVideogameEmptyTitle() {

    }

    @Test
    public void readVideogame() {

    }

    @Test
    public void readVideogameNotFound() {

    }

    @Test
    public void readVideogamesAll() {

    }

    @Test
    public void readVideogamesAllUserNotFound() {

    }

    @Test
    public void updateVideogame() {

    }

    @Test
    public void updateVideogameNot() {

    }
}
