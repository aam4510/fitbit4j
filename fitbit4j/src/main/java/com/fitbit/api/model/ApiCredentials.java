package com.fitbit.api.model;

import net.oauth.OAuthAccessor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * User: gkutlu
 * Date: Mar 16, 2010
 * Time: 3:18:15 PM
 */
@MappedSuperclass
abstract public class ApiCredentials {
    /**
     * The client which requested the credentials
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    protected APIClient client;

    @Column(length = 64, nullable = false, unique = true)
    protected String token;

    @Column(length = 64, nullable = false, unique = true)
    protected String secret;

    @Column(length = 64, nullable = true, unique = false)
    protected String resourceId;

    @Column(updatable = false, nullable = false)
    @Type(type = "com.fitbit.persistence.types.PersistentDateTime")
    private DateTime timeCreated = new DateTime();

    public ApiCredentials() {
    }

    public ApiCredentials(APIClient client, String token, String secret) {
        this.client = client;
        this.token = token;
        this.secret = secret;
    }

    public ApiCredentials(APIClient client, String token, String secret, String resourceId) {
        this(client, token, secret);
        this.resourceId = resourceId;
    }

    public APIClient getClient() {
        return client;
    }

    public void setClient(APIClient client) {
        this.client = client;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public DateTime getTimeCreated() {
        return timeCreated;
    }
    
    abstract public OAuthAccessor getOAuthAccessor();
}
