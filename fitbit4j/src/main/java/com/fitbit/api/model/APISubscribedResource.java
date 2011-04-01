package com.fitbit.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

/**
 * Records that at least one subscriber is interested in updates to an object 
 * (if {@link #resourceId} is not null) or collection of objects 
 * (if {@link #resourceId} is null}. The mapping of {@link APISubscriber}s
 * to {@link APISubscribedResource}s is accomplished via join table. This makes most
 * queries easier and more performant, at the expense of the possibility
 * of race conditions when adding subscriptions. 
 * 
 * @author MJ
 */
@Entity
public class APISubscribedResource {

	public static final Long NO_OWNER = (long) 0;
	
	@Id
	@GeneratedValue(generator="ApiSubscribedResourceIdGenerator")
	@GenericGenerator(name="ApiSubscribedResourceIdGenerator", strategy="com.fitbit.persistence.StandardPerShardHiLoGenerator",
		parameters=@Parameter(name="max_lo", value="1000")
	)
	private Long id;

	@Column(nullable=false)
	private Long resourceOwnerId;
	
	@Column(nullable=false)
	@Type(type="com.fitbit.persistence.types.GenericEnumUserType", 
		parameters=@Parameter(name="enumClass", value="com.fitbit.api.model.EventType")
	)
	private EventType resourceType;

	@Column(nullable=false)
	private Long resourceId = NO_OWNER;

	// CRITICAL API cascade?
	@OneToMany(
		targetEntity=APISubscription.class,
		fetch=FetchType.LAZY, cascade=CascadeType.ALL,
		mappedBy="subscribedResource"
	)
	private List<APISubscription> subscriptions = new ArrayList<APISubscription>();
	
	public Long getResourceOwnerId() {
		return resourceOwnerId;
	}

	public void setResourceOwnerId(Long resourceOwnerId) {
		this.resourceOwnerId = resourceOwnerId;
	}

	public EventType getResourceType() {
		return resourceType;
	}

	public void setResourceType(EventType resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
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

	public boolean isSameResource(APISubscribedResource other) {
		return 
			isSameResource(
				other.getResourceOwnerId(), 
				other.getResourceType(), 
				other.getResourceId()
			);
	}

    public boolean isSameResource(Long otherOwnerId, EventType otherResourceType, Long otherResourceId) {
        if (!otherOwnerId.equals(getResourceOwnerId())) {
            return false;
        }

        if (!(otherResourceType == getResourceType())) {
            return false;
        }

        if (NO_OWNER.equals(otherResourceId) && !NO_OWNER.equals(getResourceId())) {
            return false;
        }

        if (!NO_OWNER.equals(otherResourceId) && NO_OWNER.equals(getResourceId())) {
            return false;
        }

        if (!otherResourceId.equals(getResourceId())) {
            return false;
        }

        return true;

    }
}
