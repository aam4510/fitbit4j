package com.fitbit.api.client.subscription.storage;

import com.fitbit.api.client.LocalSubscriptionDetail;
//import com.fitbit.api.client.subscription.model.Subscription;
import com.fitbit.api.model.FitbitResourceOwner;
import com.fitbit.api.model.SubscriptionDetail;

import java.util.ArrayList;
import java.util.List;

public final class SubscriptionAdaptor {

    private SubscriptionAdaptor() {
    }

/*    public static LocalSubscriptionDetail toLocalSubscriptionDetails(Subscription subscription) {
        SubscriptionDetail subscriptionDetail = new SubscriptionDetail(subscription.getSubscriberId(), subscription.getSubscriptionId(), FitbitResourceOwner.fromIdentifier(subscription.getResourceOwnerType(), subscription.getResourceOwnerId()), subscription.getCollectionType());
        LocalSubscriptionDetail localSubscriptionDetail = new LocalSubscriptionDetail(subscriptionDetail, subscription.isKnownSubscription());
        localSubscriptionDetail.setLastUpdateNotificationDate(subscription.getLastUpdateNotificationDate());
        return localSubscriptionDetail;
    }

    public static List<LocalSubscriptionDetail> toLocalSubscriptionDetailsList(List<Subscription> subscriptions) {
        List<LocalSubscriptionDetail> detailList = new ArrayList<LocalSubscriptionDetail>(subscriptions.size());
        for(Subscription subscription : subscriptions) {
            detailList.add(toLocalSubscriptionDetails(subscription));
        }
        return detailList;
    }

    public static Subscription toSubscription(LocalSubscriptionDetail localSubscriptionDetail) {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionId(localSubscriptionDetail.getSubscriptionDetail().getSubscriptionId());
        subscription.setSubscriberId(localSubscriptionDetail.getSubscriptionDetail().getSubscriberId());
        subscription.setResourceOwnerId(localSubscriptionDetail.getSubscriptionDetail().getOwner().getId());
        subscription.setResourceOwnerType(localSubscriptionDetail.getSubscriptionDetail().getOwner().getResourceOwnerType());
        subscription.setCollectionType(localSubscriptionDetail.getSubscriptionDetail().getCollectionType());
        subscription.setKnownSubscription(localSubscriptionDetail.isKnownSubscription());
        subscription.setLastUpdateNotificationDate(localSubscriptionDetail.getLastUpdateNotificationDate());
        return subscription;
    }*/
}
