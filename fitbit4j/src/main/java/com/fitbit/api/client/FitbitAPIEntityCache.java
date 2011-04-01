package com.fitbit.api.client;

import com.fitbit.api.model.APIResourceCredentials;

/**
 * User: gkutlu
 * Date: Mar 11, 2010
 * Time: 10:28:56 AM
 */
public interface FitbitAPIEntityCache {
    Object get(APIResourceCredentials credentials, Object key);
    Object put(APIResourceCredentials credentials, Object key, Object value);
    Object remove(APIResourceCredentials credentials, Object key);
}
