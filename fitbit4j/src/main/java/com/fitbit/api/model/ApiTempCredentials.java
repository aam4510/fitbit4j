package com.fitbit.api.model;

import net.oauth.OAuthAccessor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User: gkutlu
 * Date: Mar 15, 2010
 * Time: 9:20:12 AM
 */
@Entity
public class ApiTempCredentials extends ApiCredentials {
    @Id
    @GeneratedValue(generator = "ApiTempCredentialsIdGenerator")
    @GenericGenerator(name = "ApiTempCredentialsIdGenerator", strategy = "com.fitbit.persistence.StandardPerShardHiLoGenerator")
    protected Long id;

    @Column(length = 255, nullable = true, unique = false)
    private String callback;

    @Column(length = 64, nullable = true, unique = false)
    private String verifier;

    public ApiTempCredentials() {
    }

    public ApiTempCredentials(APIClient client, String token, String secret, String callback) {
        super(client, token, secret);
        this.callback = callback;
    }

    public Long getId() {
        return id;
    }

    public String getClientKey() {
        return client.getConsumerKey();
    }

    public String getCallback() {
        return callback;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public boolean isAuthorized() {
        return resourceId != null;
    }
    
    /**
     * Temporary method which returns an OAuthAccessor instance.
     *
     * @return OAuthAccessor instance for these temporary credentials
     */
    @Override
    public OAuthAccessor getOAuthAccessor() {
        OAuthAccessor accessor = new OAuthAccessor(client.getOAuthConsumer());
        accessor.requestToken = token;
        accessor.accessToken = null;
        accessor.tokenSecret = secret;
        return accessor;
    }
}
