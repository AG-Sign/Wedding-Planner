package com.weddingPlannerBackend.IntegrationTests.ReservationServiceTest;

import com.weddingPlannerBackend.WeddingPlannerBackendApplication;
import com.weddingPlannerBackend.dtos.LimousineActivityDto;
import com.weddingPlannerBackend.dtos.UserDto;
import com.weddingPlannerBackend.mappers.ProviderMapper;
import com.weddingPlannerBackend.model.LimousineActivity;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.model.User;
import com.weddingPlannerBackend.repositories.ProviderRepo;
import com.weddingPlannerBackend.repositories.UserRepo;
import com.weddingPlannerBackend.services.ReservationService;
import com.weddingPlannerBackend.services.UserService;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteReservationTest {
    @Autowired
    UserRepo userRepo;

    @Test
    public void createActivity() throws JSONException {

        final String getUrl = "http://localhost:8080//api//users//login";

        WeddingPlannerBackendApplication.main(new String[] {});
        RestTemplate template = new RestTemplate();
        long timeKey = System.currentTimeMillis();

        // Create new provider
        User user = new User();
        user.setEmail(timeKey + "test@quadcore.com");
        user.setPassword(BCrypt.hashpw("12345", BCrypt.gensalt()));
        user.setVerified(true);
        user.setName("Dummy Provider");
        user.setAddress("Alexandria");
        userRepo.save(user);

        // Login with this provider
        HttpHeaders getHeaders = new HttpHeaders();
        byte[] encodedAuth = Base64.encodeBase64((timeKey + "test@quadcore.com:12345")
                .getBytes(StandardCharsets.US_ASCII) );
        String authHeader = "Basic " + new String( encodedAuth );
        getHeaders.set("Authorization", authHeader);
        getHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("body", getHeaders);
        ResponseEntity<String> response = template.exchange(getUrl, HttpMethod.GET, entity, String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        // Get login token
        JSONObject jsonObject= new JSONObject(response.getBody());
        String token = jsonObject.get("token").toString();
        String url = "http://localhost:8080//api//reservations";

        // Set provider token in post request header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        // Valid activity
        JSONObject reservationJson = new JSONObject();
        reservationJson.put("date", "02/12/2022");

        // Create activity
        response = template.postForEntity(url,
                new HttpEntity<String>(reservationJson.toString(), headers), String.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        entity = new HttpEntity<String>(null, headers);
        response = template.exchange(url, HttpMethod.DELETE, entity, String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        reservationJson.put("date", "10/02/2022");
        response = template.postForEntity(url,
                new HttpEntity<String>(reservationJson.toString(), headers), String.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        HttpEntity<String> finalEntity = entity;
        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.exchange(url, HttpMethod.DELETE, finalEntity, String.class);
        });
    }
}
