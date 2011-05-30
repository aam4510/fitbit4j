package com.fitbit.api.client.subscription.storage;

import com.fitbit.api.client.FitbitApiSubscriptionStorage;
import com.fitbit.api.client.LocalSubscriptionDetail;
import com.fitbit.api.client.subscription.dao.ISubscriptionDao;

import javax.annotation.Resource;
import java.util.List;

public class PersistenceFitbitApiSubscriptionStorage implements FitbitApiSubscriptionStorage {

    @Resource
    private ISubscriptionDao subscriptionDao;

    @Override
    public void save(LocalSubscriptionDetail subscription) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public LocalSubscriptionDetail getBySubscriptionId(String subscriptionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(LocalSubscriptionDetail subscription) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<LocalSubscriptionDetail> getAllSubscriptions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
