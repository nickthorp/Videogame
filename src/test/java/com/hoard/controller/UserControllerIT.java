package com.hoard.controller;

import com.hoard.entity.User;
import com.hoard.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @Autowired
    private UserRepository userRepository;
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/api/");
        User user1 = new User("user1@email.com", "xxUserxx", "Scratch", "Pheonix");
        User user2 = new User("user2@email.com", "Flergington", "First", "Last");
        User user3 = new User("user3@email.com", "DankMemeGod", "Bob", "Everyman");
        userRepository.save(user1);
        //userRepository.save(user2);
        //userRepository.save(user3);
    }

    @After
    public void tearDown() throws Exception {
        userRepository.delete(userRepository.findByEmail("user1@email.com"));
        //userRepository.delete(userRepository.findOne(2));
        //userRepository.delete(userRepository.findOne(3));
    }

    @Test
    public void getHello() {
        ResponseEntity<String> response = template.getForEntity(base.toString().concat("user/get/9"),
                String.class);
        assertThat(response.getBody(), equalTo("User not found."));
    }

    @Test
    public void getNonExistentUser() {
        assertThat( get( base.toString().concat("user/get/8") ).asString(), equalTo("User not found.") );
    }

    @Test
    public void getUser() {
        get(base.toString().concat("user/get/1")).then().body("id", equalTo(2));
        //assertThat( get( base.toString().concat("user/get/1") ).asString(), equalTo("User not found.") );
    }

}