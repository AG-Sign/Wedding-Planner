package com.weddingPlannerBackend.IntegrationTests;

import com.weddingPlannerBackend.WeddingPlannerBackendApplication;
import com.weddingPlannerBackend.model.Provider;
import com.weddingPlannerBackend.repositories.ProviderRepo;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateActivityTest {
    @Autowired
    ProviderRepo providerRepo;

    @Test
    public void createActivity() throws JSONException {

        final String getUrl = "http://localhost:8080//api//providers//login";

        WeddingPlannerBackendApplication.main(new String[] {});
        RestTemplate template = new RestTemplate();
        long timeKey = System.currentTimeMillis();

        // Create new provider
        Provider provider = new Provider();
        provider.setEmail(timeKey + "test@quadcore.com");
        provider.setPassword(BCrypt.hashpw("12345", BCrypt.gensalt()));
        provider.setVerified(true);
        provider.setName("Dummy Provider");
        provider.setAddress("Alexandria");
        providerRepo.save(provider);

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

        // List get activities count before adding a new one
        String limActUrl = "http://localhost:8080//api//limousine_activities";
        entity = new HttpEntity<>("body", getHeaders);
        response = template.exchange(limActUrl, HttpMethod.GET, entity, String.class);
        JSONArray jsonArray = new JSONArray(response.getBody());
        int limousineActNo = jsonArray.length();

        // Set provider token in post request header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        // Valid activity
        JSONObject limousineActivity = new JSONObject();
        limousineActivity.put("carType", "Lada");
        limousineActivity.put("price", 1200.25);
        limousineActivity.put("img", "https://media.istockphoto.com/photos/chain-with-red-link-picture-id529415724");

        // Create activity
        response = template.postForEntity(limActUrl,
                new HttpEntity<String>(limousineActivity.toString(), headers), String.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Check listed activities count increased by one
        entity = new HttpEntity<>("body", getHeaders);
        response = template.exchange(limActUrl, HttpMethod.GET, entity, String.class);
        jsonArray = new JSONArray(response.getBody());
        Assertions.assertEquals(limousineActNo+1, jsonArray.length());
    }
}
