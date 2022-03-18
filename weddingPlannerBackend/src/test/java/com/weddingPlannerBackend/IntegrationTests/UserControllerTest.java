package com.weddingPlannerBackend.IntegrationTests;

import com.weddingPlannerBackend.WeddingPlannerBackendApplication;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.repositories.UserRepo;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    static private long timeKey;

    @Autowired
    UserRepo userRepo;

    @Test
    @Order(1)
    void registerUserAccount() throws JSONException {

        final String postUrl = "http://localhost:8080//api//users//";

        timeKey = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Valid user
        JSONObject validUser = new JSONObject();
        validUser.put("name", "Test1");
        validUser.put("password", "12345");
        validUser.put("address", "Alexandria");
        validUser.put("email", timeKey + "test@quadcore.com");

        // Invalid duplicated user
        JSONObject duplicateUser = new JSONObject();
        duplicateUser.put("name", "Test2");
        duplicateUser.put("password", "12345");
        duplicateUser.put("address", "Alexandria");
        duplicateUser.put("email", timeKey + "test@quadcore.com");

        // Invalid user (no name)
        JSONObject invalidUser1 = new JSONObject();
        invalidUser1.put("name", null);
        invalidUser1.put("password", "12345");
        invalidUser1.put("address", "Alexandria");
        invalidUser1.put("email", System.currentTimeMillis() + "test@quadcore.com");

        // Invalid user (no password)
        JSONObject invalidUser2 = new JSONObject();
        invalidUser2.put("name", "Test4");
        invalidUser2.put("password", null);
        invalidUser2.put("address", "Alexandria");
        invalidUser2.put("email", System.currentTimeMillis() + "test@quadcore.com");

        // Invalid user (wrong email format)
        JSONObject invalidUser3 = new JSONObject();
        invalidUser3.put("name", "Test5");
        invalidUser3.put("password", "12345");
        invalidUser3.put("address", "Alexandria");
        invalidUser3.put("email", "@quadcore.com");

        WeddingPlannerBackendApplication.main(new String[] {});
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> response = template.postForEntity(postUrl,
                new HttpEntity<String>(validUser.toString(), headers), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(duplicateUser.toString(), headers), String.class);
        });

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(invalidUser1.toString(), headers), String.class);
        });

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(invalidUser2.toString(), headers), String.class);
        });

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(invalidUser3.toString(), headers), String.class);
        });
    }

    @Test
    @Order(2)
    void login() throws JSONException {

        final String getUrl = "http://localhost:8080//api//users//login";

        RestTemplate template = new RestTemplate();
        timeKey = System.currentTimeMillis();
        User user = new User();
        user.setEmail(timeKey + "test@quadcore.com");
        user.setPassword(BCrypt.hashpw("12345", BCrypt.gensalt()));
        user.setVerified(true);
        user.setName("Dummy User");
        user.setAddress("Alexandria");
        userRepo.save(user);

        HttpHeaders getHeaders = new HttpHeaders();
        byte[] encodedAuth = Base64.encodeBase64((timeKey + "test@quadcore.com:12345")
                                   .getBytes(StandardCharsets.US_ASCII) );
        String authHeader = "Basic " + new String( encodedAuth );
        getHeaders.set("Authorization", authHeader);
        getHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", getHeaders);
        ResponseEntity<String> response = template.exchange(getUrl, HttpMethod.GET, entity, String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
