package com.fitbit.api.model;

import net.oauth.OAuthServiceProvider;

/**
 * User: gkutlu
 * Date: Feb 27, 2010
 * Time: 9:29:43 PM
 */
public class APIServer extends OAuthServiceProvider {
    private static final long serialVersionUID = 3330234264581976072L;
    String serverId;

    public APIServer(String serverId, String requestTokenURL, String userAuthorizationURL, String accessTokenURL) {
        super(requestTokenURL, userAuthorizationURL, accessTokenURL);
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }
}
