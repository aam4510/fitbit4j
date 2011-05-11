package com.fitbit.web.context;

import com.fitbit.api.client.FitbitApiClientAgent;
import com.fitbit.api.client.LocalUserDetail;
import com.fitbit.api.client.service.FitbitAPIClientService;
import org.joda.time.LocalDate;

public class RequestContext {

    private LocalDate parsedLocalDate = new LocalDate();
    private FitbitAPIClientService<FitbitApiClientAgent> apiClientService;
    private LocalUserDetail ourUser;

    public LocalDate getParsedLocalDate() {
        return parsedLocalDate;
    }

    public FitbitAPIClientService<FitbitApiClientAgent> getApiClientService() {
        return apiClientService;
    }

    public void setApiClientService(FitbitAPIClientService<FitbitApiClientAgent> apiClientService) {
        this.apiClientService = apiClientService;
    }

    public LocalUserDetail getOurUser() {
        return ourUser;
    }

    public void setOurUser(LocalUserDetail ourUser) {
        this.ourUser = ourUser;
    }
}
