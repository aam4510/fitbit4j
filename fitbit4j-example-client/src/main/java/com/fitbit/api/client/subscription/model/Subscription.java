package com.fitbit.api.client.subscription.model;

import com.fitbit.api.model.APICollectionType;
import com.fitbit.api.model.ResourceOwnerType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "resource_owner_id")
    private String resourceOwnerId;

    @Column(name = "resource_owner_type")
    @Enumerated(value = EnumType.STRING)
    private ResourceOwnerType resourceOwnerType;

    @Column(name = "collection_type")
    @Enumerated(value = EnumType.STRING)
    private APICollectionType collectionType;

    @Column(name = "known_subscription")
    private boolean knownSubscription;

    @Column(name = "last_update_notification_date")
    private Date lastUpdateNotificationDate;

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getResourceOwnerId() {
        return resourceOwnerId;
    }

    public void setResourceOwnerId(String resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
    }

    public ResourceOwnerType getResourceOwnerType() {
        return resourceOwnerType;
    }

    public void setResourceOwnerType(ResourceOwnerType resourceOwnerType) {
        this.resourceOwnerType = resourceOwnerType;
    }

    public APICollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(APICollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public boolean isKnownSubscription() {
        return knownSubscription;
    }

    public void setKnownSubscription(boolean knownSubscription) {
        this.knownSubscription = knownSubscription;
    }

    public Date getLastUpdateNotificationDate() {
        return lastUpdateNotificationDate;
    }

    public void setLastUpdateNotificationDate(Date lastUpdateNotificationDate) {
        this.lastUpdateNotificationDate = lastUpdateNotificationDate;
    }
}
