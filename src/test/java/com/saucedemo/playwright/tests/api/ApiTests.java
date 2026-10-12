package com.saucedemo.playwright.tests.api;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.saucedemo.playwright.base.PlaywrightApiTestBase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTests extends PlaywrightApiTestBase {
    @Test
    public void existingPostCanBeRetrieved() {
        APIResponse response = apiRequest.get("/posts/1");

        logRequestAndResponse("GET", response, null);
        Assert.assertEquals(response.status(), 200);
        Assert.assertTrue(response.text().matches("(?s).*\"userId\"\\s*:\\s*1.*"));
        Assert.assertTrue(response.text().matches("(?s).*\"id\"\\s*:\\s*1.*"));
        Assert.assertTrue(response.text().contains("sunt aut facere"));
    }

    @Test
    public void postsCanBeFilteredByUser() {
        APIResponse response = apiRequest.get("/posts?userId=1");

        logRequestAndResponse("GET", response, null);
        Assert.assertEquals(response.status(), 200);
        Assert.assertTrue(response.text().startsWith("["));
        Assert.assertTrue(response.text().contains("\"userId\": 1"));
        Assert.assertFalse(response.text().contains("\"userId\": 2"));
    }

    @Test
    public void commentsCanBeRetrievedForAPost() {
        APIResponse response = apiRequest.get("/posts/1/comments");

        logRequestAndResponse("GET", response, null);
        Assert.assertEquals(response.status(), 200);
        Assert.assertTrue(response.text().startsWith("["));
        Assert.assertTrue(response.text().contains("\"postId\": 1"));
        Assert.assertTrue(response.text().contains("\"email\""));
    }

    @Test
    public void postCanBeCreated() {
        String requestBody = """
                {
                  "title": "Playwright API test",
                  "body": "Created by the API automation suite",
                  "userId": 1
                }
                """;
        APIResponse response = apiRequest.post("/posts", jsonRequest(requestBody));

        logRequestAndResponse("POST", response, requestBody);
        Assert.assertEquals(response.status(), 201);
        Assert.assertTrue(response.text().contains("Playwright API test"));
        Assert.assertTrue(response.text().matches("(?s).*\"id\"\\s*:\\s*\\d+.*"));
    }

    @Test
    public void postCanBeFullyUpdated() {
        String requestBody = """
                {
                  "id": 1,
                  "title": "Updated by Playwright",
                  "body": "Full update API test",
                  "userId": 1
                }
                """;
        APIResponse response = apiRequest.put("/posts/1", jsonRequest(requestBody));

        logRequestAndResponse("PUT", response, requestBody);
        Assert.assertEquals(response.status(), 200);
        Assert.assertTrue(response.text().contains("Updated by Playwright"));
        Assert.assertTrue(response.text().contains("Full update API test"));
    }

    @Test
    public void postCanBePartiallyUpdated() {
        String requestBody = """
                {
                  "title": "Partially updated by Playwright"
                }
                """;
        APIResponse response = apiRequest.patch("/posts/1", jsonRequest(requestBody));

        logRequestAndResponse("PATCH", response, requestBody);
        Assert.assertEquals(response.status(), 200);
        Assert.assertTrue(response.text().contains("Partially updated by Playwright"));
        Assert.assertTrue(response.text().contains("\"id\": 1"));
    }

    @Test
    public void postCanBeDeleted() {
        APIResponse response = apiRequest.delete("/posts/1");

        logRequestAndResponse("DELETE", response, null);
        Assert.assertEquals(response.status(), 200);
    }

    @Test
    public void missingPostReturnsNotFound() {
        APIResponse response = apiRequest.get("/posts/9999");

        logRequestAndResponse("GET", response, null);
        Assert.assertEquals(response.status(), 404);
    }

    private RequestOptions jsonRequest(String body) {
        return RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(body);
    }

    private void logRequestAndResponse(String method, APIResponse response, String requestBody) {
        System.out.printf("%n>>> %s %s%n", method, response.url());
        if (requestBody != null) {
            System.out.println("Request body:\n" + requestBody);
        }
        System.out.printf(
                "<<< Response: %d %s%nContent-Type: %s%nResponse body:%n%s%n",
                response.status(),
                response.statusText(),
                response.headers().getOrDefault("content-type", "unknown"),
                response.text());
    }
}
