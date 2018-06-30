package com.hoard.videogame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoard.user.entity.User;
import com.hoard.videogame.entity.Videogame;
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
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.http.MediaType;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private UserRepository userRepository2;

    private User user = new User(1,"user@email.com", "xxUserxx", "Scratch", "Phoenix");
    private Videogame vg = new Videogame(1, user, "Super Mario", "Nintendogs", "SNES", false, false, true);
    private Videogame vgNoID = new Videogame(user, "Super Mario", "Nintendogs", "SNES", false, false, true);

    @Configuration
    @EnableAutoConfiguration
    @EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING)
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
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(videogameRepository.save(vgNoID)).thenReturn(vg);
        mockMvc.perform(
                post("/api/videogame/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vgNoID))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(vg.getId())))
                .andExpect(jsonPath("user.email", is("user@email.com")));
    }

    @Test
    public void createVideogameNoUserID() throws Exception {
        mockMvc.perform(
                post("/api/videogame/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vgNoID))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void createVideogameUserNotFound() throws Exception {
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(
                post("/api/videogame/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vgNoID))
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code", is(1)));
    }

    @Test
    public void createVideogameExtraID() throws Exception {
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.of(user));
        mockMvc.perform(
                post("/api/videogame/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vg))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(8)));
    }

    @Test
    public void createVideogameNullJSON() throws Exception {
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.of(user));
        mockMvc.perform(
                post("/api/videogame/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void createVideogameEmptyTitle() throws Exception {
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.of(user));
        Videogame game = vgNoID;
        game.setTitle("");
        mockMvc.perform(
                post("/api/videogame/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(game))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(7)));
    }

    @Test
    public void readVideogame() throws Exception {
        Mockito.when(videogameRepository.findById(1)).thenReturn(Optional.of(vg));
        mockMvc.perform(
                get("/api/videogame/get/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("user.email", is("user@email.com")));
    }

    @Test
    public void readVideogameNotFound() throws Exception {
        Mockito.when(videogameRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(
                get("/api/videogame/get/1")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code", is(1)));
    }

    @Test
    public void readVideogameAll() throws Exception {
        ArrayList<Videogame> games = new ArrayList<>();
        Videogame vg2 = new Videogame(2, user, "Super Mario World", "Nintendo", "NES", false, true, true);
        Videogame vg3 = new Videogame(3, user, "Super Mario World 2", "Nintendo", "SNES", true, true, true);
        games.add(vg);
        games.add(vg2);
        games.add(vg3);
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(videogameRepository.findByUserId(1)).thenReturn(games);
        mockMvc.perform(
                get("/api/videogame/get/all/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1,2,3)));
    }

    @Test
    public void readVideogameAllUserNotFound() throws Exception {
        Mockito.when(userRepository2.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(
                get("/api/videogame/get/all/1")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code", is(1)));
    }

    @Test
    public void updateVideogame() throws Exception {
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(videogameRepository.findById(1)).thenReturn(Optional.of(vg));
        Videogame update = new Videogame(1, user, "Super Luigi World", "Ninpo", "NES", false, true, false);
        Mockito.when(videogameRepository.save(update)).thenReturn(update);
        mockMvc.perform(
                put("/api/videogame/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(update))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("title", is("Super Luigi World")));
    }

    @Test
    public void updateVideogameNotFound() throws Exception {
        Mockito.when(userRepository2.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(videogameRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(
                put("/api/videogame/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vg))
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code", is(1)));
    }

    @Test
    public void updateVideogameNoChange() throws Exception {
        Mockito.when(userRepository2.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(videogameRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(vg));
        Mockito.when(videogameRepository.save(vg)).thenReturn(vg);
        mockMvc.perform(
                put("/api/videogame/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vg))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("code", is(0)));
    }

    @Test
    public void updateVideogameMismatchIds() throws Exception {
        Videogame update = new Videogame(2, user, "Super Mario", "Nintendogs", "SNES", false, false, true);
        Mockito.when(userRepository2.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(videogameRepository.findById(1)).thenReturn(Optional.of(vg));
        mockMvc.perform(
                put("/api/videogame/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(update))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code", is(4)));
    }

    //TODO Add more update tests

    @Test
    public void deleteVideogame() throws Exception {
        Mockito.when(userRepository2.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(videogameRepository.findById(1)).thenReturn(Optional.of(vg), Optional.of(vg), Optional.empty());
        mockMvc.perform(
                delete("/api/videogame/delete/1")
        ).andExpect(status().isOk());
    }

    //TODO Add move delete tests
}

