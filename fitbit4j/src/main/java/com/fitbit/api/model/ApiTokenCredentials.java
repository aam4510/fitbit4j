package com.fitbit.api.model;

import net.oauth.OAuthAccessor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User: gkutlu
 * Date: Mar 15, 2010
 * Time: 9:21:08 AM
 */
@Entity
public class ApiTokenCredentials extends ApiCredentials {
    @Id
    @GeneratedValue(generator = "ApiTokenCredentialsIdGenerator")
    @GenericGenerator(name = "ApiTokenCredentialsIdGenerator", strategy = "com.fitbit.persistence.StandardPerShardHiLoGenerator")
    private Long id;

    public ApiTokenCredentials() {
    }

    public ApiTokenCredentials(APIClient client, String token, String secret, String resourceId) {
        super(client, token, secret, resourceId);
    }

    public Long getId() {
        return id;
    }

    @Override
    public OAuthAccessor getOAuthAccessor() {
        OAuthAccessor accessor = new OAuthAccessor(client.getOAuthConsumer());
        accessor.requestToken = null;
        accessor.accessToken = token;
        accessor.tokenSecret = secret;
        return accessor;
    }
}
