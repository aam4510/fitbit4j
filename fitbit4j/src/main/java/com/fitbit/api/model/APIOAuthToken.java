package com.fitbit.api.model;

/**
 * User: gkutlu
 * Date: Feb 27, 2010
 * Time: 4:51:48 PM
 */
public class APIOAuthToken {
    APIClient client;
    String resourceId;
    String token;
    Long userId;

    public APIOAuthToken(APIClient client, String resourceId, String token, Long userId) {
        this.client = client;
        this.resourceId = resourceId;
        this.token = token;
        this.userId = userId;
    }

    public APIClient getConsumer() {
        return client;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }
}
