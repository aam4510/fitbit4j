package com.fitbit.api.client.subscription.dao.impl;

import com.fitbit.api.client.subscription.dao.ISubscriptionDao;
import com.fitbit.api.client.subscription.model.Subscription;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDao extends GenericDao<Subscription, Long> implements ISubscriptionDao {
}
