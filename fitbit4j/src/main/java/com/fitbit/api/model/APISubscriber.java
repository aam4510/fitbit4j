package com.fitbit.api.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * An API subscriber is a client endpoint that accepts near real-time update
 * notifications. An {@link APIClient} may have zero or more subscribers. Each subscriber
 * may have zero or more {@link APISubscribedResource}s.
 *
 * @author MJ
 */
@Entity
public class APISubscriber {

    @Id
    @GeneratedValue(generator = "ApiSubscriberIdGenerator")
    @GenericGenerator(name = "ApiSubscriberIdGenerator", strategy = "com.fitbit.persistence.AlphaSafeHiLoTableGenerator",
            parameters = {
                    @Parameter(name = "table", value = "hilo_generator_table"),
                    @Parameter(name = "primary_key_column", value = "generator_key"),
                    @Parameter(name = "value_column", value = "hi_value"),
                    @Parameter(name = "primary_key_value", value = "APISubscriber_ID"),
                    @Parameter(name = "max_lo", value = "0")
            }
    )
    private Long id;

    // CRITICAL API cascade?
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = true, name = "apiClientId")
    private APIClient client;

    @Column(nullable = false)
    private String clientAssignedSubscriberId;

    @Column(nullable = false)
    private String endpointUrl;

    @Column(nullable = false)
    @Type(type = "com.fitbit.persistence.types.GenericEnumUserType",
            parameters = @Parameter(name = "enumClass", value = "com.fitbit.api.model.APIFormat")
    )
    private APIFormat format;

    @Column(nullable = false)
    @Type(type = "com.fitbit.persistence.types.GenericEnumUserType",
            parameters = @Parameter(name = "enumClass", value = "com.fitbit.api.model.APIVersion")
    )
    private APIVersion apiVersion;

    @Column(nullable = false)
    @Type(type = "com.fitbit.persistence.types.PersistentDateTime")
    private DateTime createDate;

    @Column(nullable = true)
    @Type(type = "com.fitbit.persistence.types.PersistentDateTime")
    private DateTime lastSuccessfulUpdateTime;

    @Column(nullable = true)
    @Type(type = "com.fitbit.persistence.types.PersistentDateTime")
    private DateTime lastAttemptedUpdateTime;

    @Column
    private int totalErrors = 0;

    @Column
    private int totalUpdates = 0;

    @Column
    private int recentConsecutiveErrors = 0;

    @Column(nullable = true)
    private Long lastUpdateVersion;

    public APISubscriber() {
        this.createDate = new DateTime();
    }

    // CRITICAL API cascade?
    @OneToMany(mappedBy = "subscriber", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<APISubscription> subscriptions = new ArrayList<APISubscription>();

    public APIClient getClient() {
        return client;
    }

    public void setClient(APIClient apiClient) {
        this.client = apiClient;
    }

    public String getClientAssignedSubscriberId() {
        return clientAssignedSubscriberId;
    }

    public void setClientAssignedSubscriberId(String clientAssignedSubscriberId) {
        this.clientAssignedSubscriberId = clientAssignedSubscriberId;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public APIFormat getFormat() {
        return format;
    }

    public void setFormat(APIFormat format) {
        this.format = format;
    }

    public APIVersion getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(APIVersion apiVersion) {
        this.apiVersion = apiVersion;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public DateTime getLastSuccessfulUpdateTime() {
        return lastSuccessfulUpdateTime;
    }

    public void setLastSuccessfulUpdateTime(DateTime lastSuccessfulUpdateTime) {
        this.lastSuccessfulUpdateTime = lastSuccessfulUpdateTime;
    }

    public DateTime getLastAttemptedUpdateTime() {
        return lastAttemptedUpdateTime;
    }

    public void setLastAttemptedUpdateTime(DateTime lastAttemptedUpdateTime) {
        this.lastAttemptedUpdateTime = lastAttemptedUpdateTime;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
    }

    public int getTotalUpdates() {
        return totalUpdates;
    }

    public void setTotalUpdates(int totalUpdates) {
        this.totalUpdates = totalUpdates;
    }

    public int getRecentConsecutiveErrors() {
        return recentConsecutiveErrors;
    }

    public void setRecentConsecutiveErrors(int recentConsecutiveErrors) {
        this.recentConsecutiveErrors = recentConsecutiveErrors;
    }

    public Long getLastUpdateVersion() {
        return lastUpdateVersion;
    }

    public void setLastUpdateVersion(Long lastUpdateVersion) {
        this.lastUpdateVersion = lastUpdateVersion;
    }

    public Long getId() {
        return id;
    }

    public List<APISubscription> getSubscriptions() {
        return Collections.unmodifiableList(subscriptions);
    }

    public void addSubscription(APISubscription subscription) {
        subscriptions.add(subscription);
    }

    public void indicateAttempt() {
        setLastAttemptedUpdateTime(new DateTime());
    }

    public void indicateSuccess(Long version) {
        setLastSuccessfulUpdateTime(new DateTime());
        setRecentConsecutiveErrors(0);
        setLastUpdateVersion(version);
        setTotalUpdates(getTotalUpdates() + 1);
    }

    public void indicateError() {
        setRecentConsecutiveErrors(getRecentConsecutiveErrors() + 1);
        setTotalErrors(getTotalErrors() + 1);
    }
}
