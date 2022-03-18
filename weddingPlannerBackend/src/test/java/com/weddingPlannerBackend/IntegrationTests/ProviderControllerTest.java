package com.weddingPlannerBackend.IntegrationTests;

import com.weddingPlannerBackend.WeddingPlannerBackendApplication;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class ProviderControllerTest {

    @Test
    void registerProviderAccount() throws JSONException {

        final String postUrl = "http://localhost:8080//api//providers//";

        long timeKey = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Valid provider
        JSONObject validProvider = new JSONObject();
        validProvider.put("name", "Test1");
        validProvider.put("password", "12345");
        validProvider.put("address", "Alexandria");
        validProvider.put("email", timeKey + "test@quadcore.com");

        // Invalid duplicated user
        JSONObject duplicateProvider = new JSONObject();
        duplicateProvider.put("name", "Test2");
        duplicateProvider.put("password", "12345");
        duplicateProvider.put("address", "Alexandria");
        duplicateProvider.put("email", timeKey + "test@quadcore.com");

        // Invalid provider (no name)
        JSONObject invalidProvider1 = new JSONObject();
        invalidProvider1.put("name", null);
        invalidProvider1.put("password", "12345");
        invalidProvider1.put("address", "Alexandria");
        invalidProvider1.put("email", System.currentTimeMillis() + "test@quadcore.com");

        // Invalid provider (no password)
        JSONObject invalidProvider2 = new JSONObject();
        invalidProvider2.put("name", "Test4");
        invalidProvider2.put("password", null);
        invalidProvider2.put("address", "Alexandria");
        invalidProvider2.put("email", System.currentTimeMillis() + "test@quadcore.com");

        // Invalid provider (wrong email format)
        JSONObject invalidProvider3 = new JSONObject();
        invalidProvider3.put("name", "Test5");
        invalidProvider3.put("password", "12345");
        invalidProvider3.put("address", "Alexandria");
        invalidProvider3.put("email", "@quadcore.com");

        WeddingPlannerBackendApplication.main(new String[] {});
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> response = template.postForEntity(postUrl,
                new HttpEntity<String>(validProvider.toString(), headers), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(duplicateProvider.toString(), headers), String.class);
        });

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(invalidProvider1.toString(), headers), String.class);
        });

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(invalidProvider2.toString(), headers), String.class);
        });

        Assertions.assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            template.postForEntity(postUrl,
                    new HttpEntity<String>(invalidProvider3.toString(), headers), String.class);
        });
    }
}
