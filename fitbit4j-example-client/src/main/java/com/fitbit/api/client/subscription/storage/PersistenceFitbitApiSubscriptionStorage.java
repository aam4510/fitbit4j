package com.fitbit.api.client.subscription.storage;

import com.fitbit.api.client.FitbitApiSubscriptionStorage;
import com.fitbit.api.client.LocalSubscriptionDetail;
import com.fitbit.api.client.subscription.dao.ISubscriptionDao;
import com.fitbit.api.client.subscription.model.Subscription;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
public class PersistenceFitbitApiSubscriptionStorage implements FitbitApiSubscriptionStorage {

    @Resource
    private ISubscriptionDao subscriptionDao;

    @Override
    public void save(LocalSubscriptionDetail localSubscription) {
        Subscription subscription = subscriptionDao.findSingleByQuery("FROM Subscription s WHERE s.subscriptionId = ?", localSubscription.getSubscriptionDetail().getSubscriptionId());
        if (subscription == null) {
            subscriptionDao.persist(SubscriptionAdaptor.toSubscription(localSubscription));
        }
    }

    @Override
    public LocalSubscriptionDetail getBySubscriptionId(String subscriptionId) {
        Subscription subscription = subscriptionDao.findSingleByQuery("FROM Subscription s WHERE s.subscriptionId = ?", subscriptionId);
        return subscription != null ? SubscriptionAdaptor.toLocalSubscriptionDetails(subscription) : null;
    }

    @Override
    public void delete(LocalSubscriptionDetail localSubscription) {
        Subscription subscription = subscriptionDao.findSingleByQuery("FROM Subscription s WHERE s.subscriptionId = ?", localSubscription.getSubscriptionDetail().getSubscriptionId());
        if (subscription != null) {
            subscriptionDao.remove(subscription);
        }
    }

    @Override
    public List<LocalSubscriptionDetail> getAllSubscriptions() {
        return SubscriptionAdaptor.toLocalSubscriptionDetailsList(subscriptionDao.findAll());
    }
}
