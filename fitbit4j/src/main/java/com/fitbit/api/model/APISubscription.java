package com.fitbit.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class APISubscription {

	@Id
	@GeneratedValue(generator="ApiSubscriptionIdGenerator")
	@GenericGenerator(name="ApiSubscriptionIdGenerator", strategy="com.fitbit.persistence.StandardPerShardHiLoGenerator")
	private Long id;

	@Column(nullable=false)
	private String clientAssignedSubscriptionId;

	// REVIEW API cascade subscriber <- subscription
	@ManyToOne(fetch=FetchType.LAZY, optional=false, cascade={CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(nullable=false, updatable=true, name="apiSubscriberId")
	private APISubscriber subscriber;

	// CRITICAL API cascade?
	@ManyToOne(fetch=FetchType.LAZY, optional=false, cascade=CascadeType.ALL)
	@JoinColumn(nullable=false, updatable=true, name="apiSubscribedResourceId")
	private APISubscribedResource subscribedResource;

	@Column(nullable=false)
	@Type(type="com.fitbit.persistence.types.PersistentDateTime")
	private DateTime createDate;

	
	public String getClientAssignedSubscriptionId() {
		return clientAssignedSubscriptionId;
	}

	public void setClientAssignedSubscriptionId(String clientAssignedSubscriptionId) {
		this.clientAssignedSubscriptionId = clientAssignedSubscriptionId;
	}

	public APISubscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(APISubscriber apiSubscriber) {
        subscriber = apiSubscriber;
	}

	public APISubscribedResource getSubscribedResource() {
		return subscribedResource;
	}

	public void setSubscribedResource(APISubscribedResource apiSubscribedResource) {
        subscribedResource = apiSubscribedResource;
	}

	public DateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(DateTime createDate) {
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}
	
}
