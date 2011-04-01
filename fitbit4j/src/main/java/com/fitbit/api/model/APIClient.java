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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import net.oauth.OAuthConsumer;
import org.joda.time.DateTime;

/**
 * An API Client is a third-party application that consumes the Fitbit API via
 * the use of an OAuth consumer key and shared secret. Some fields are required
 * only for web applications and are invalid for client applications.  
 * 
 * User: gkutlu
 * Date: Feb 22, 2010
 * Time: 1:24:22 PM
 */
@Entity
public class APIClient {
	
	@Id
	@GeneratedValue(generator="ApiClientIdGenerator")
    @GenericGenerator(name = "ApiClientIdGenerator", strategy = "com.fitbit.persistence.AlphaSafeHiLoTableGenerator",
                      parameters = {
                              @Parameter(name = "table", value = "hilo_generator_table"),
                              @Parameter(name = "primary_key_column", value = "generator_key"),
                              @Parameter(name = "value_column", value = "hi_value"),
                              @Parameter(name = "primary_key_value", value = "APIClient_ID"),
                              @Parameter(name = "max_lo", value = "0")
                      }
    )
	private Long id;

    @Column(nullable = true, updatable = false)
    private Long userId;

	@Column(nullable=false)
    private String name;

	@Column(nullable=false)
    private String description;

	@Column(nullable=false)
    private String url;

	@Column(nullable=false)
	@Type(type="com.fitbit.persistence.types.GenericEnumUserType", 
		parameters=@Parameter(name="enumClass", value="com.fitbit.api.model.APIApplicationType")
	)	
    private APIApplicationType type;

	@Column(nullable=false)
	@Type(type="com.fitbit.persistence.types.GenericEnumUserType", 
		parameters=@Parameter(name="enumClass", value="com.fitbit.api.model.APIAccessType")
	)	
    private APIAccessType accessType;

	@Column(nullable=true)
    private String callback;

	@Column(nullable=false, unique=true)
    private String consumerKey;

	@Column(nullable=false)
    private String secret;

	@Column(nullable=false)
    private Boolean useLogin;
	
	@Column(nullable=false)
    private String org;

	@Column(nullable=false)
    private String orgUrl;

    @Type(type="text")
	@Column(nullable=true)
	private String adminNotes;

    @Column
    @Type(type = "com.fitbit.persistence.types.PersistentDateTime")
    private DateTime createdOn;

	// CRITICAL API cascade?
	@OneToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(nullable=true, updatable=true, name="defaultSubscriberId")
    private APISubscriber defaultSubscriber;

	// CRITICAL API cascade?
    @OneToMany(mappedBy="client", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private List<APISubscriber> subscribers = new ArrayList<APISubscriber>();

    @Column(nullable=false)
    private long countHitsLimitReached;

    @Column
    @Type(type = "com.fitbit.persistence.types.PersistentDateTime")
    private DateTime lastHitsLimitReached;

    public APIClient() {
        createdOn = new DateTime();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Application name
     */
    public String getName() {
        return name;
    }

	/**
     * Application description
     */
    public String getDescription() {
        return description;
    }

	/**
     * Application web site URL
     */
    public String getUrl() {
        return url;
    }

	/**
     * Application type. One of CLIENT or BROWSER.
     */
    public APIApplicationType getType() {
        return type;
    }

	/**
     * Application access type. One of READ or READ_WRITE.
     */
    public APIAccessType getAccessType() {
        return accessType;
    }

    /**
     * Checks is access type read-only
     * @return true if access type read only
     */
    public boolean isReadAccessType() {
        return APIAccessType.READ == getAccessType();
    }

    public boolean isReadWriteAccessType() {
        return APIAccessType.READ_WRITE == getAccessType();
    }

	/**
     * Application callback URL. Optional and not provided for client applications.
     */
    public String getCallback() {
        return callback;
    }

	/**
     * Does the client application intend to use Fitbit for authentication?
     */
    public Boolean isUseLogin() {
        return useLogin;
    }

    /**
     * Consumer company or organization.
     */
    public String getOrg() {
        return org;
    }

	/**
     * The home page of the client company or organization.
     */
    public String getOrgUrl() {
        return orgUrl;
    }

	/**
     * Unique application key
     */
    public String getConsumerKey() {
        return consumerKey;
    }

	/**
     * Unique application secret
     */
    public String getSecret() {
        return secret;
    }

    public Boolean getUseLogin() {
		return useLogin;
	}

	public void setUseLogin(Boolean useLogin) {
		this.useLogin = useLogin;
	}

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public APISubscriber getDefaultSubscriber() {
		return defaultSubscriber;
	}

	public void setDefaultSubscriber(APISubscriber defaultSubscriber) {
		if ( ! subscribers.contains(defaultSubscriber)) {
			throw new IllegalArgumentException("Default subscriber must be a registered subscriber.");
		}
		this.defaultSubscriber = defaultSubscriber;
	}

	public Long getId() {
		return id;
	}

	public List<APISubscriber> getSubscribers() {
		return Collections.unmodifiableList(subscribers);
	}
	
	public void addSubscriber(APISubscriber subscriber) {
		subscriber.setClient(this);
		subscribers.add(subscriber);
	}

    public void deleteSubscriber(APISubscriber subscriber) {
        subscriber.setClient(null);
        subscribers.remove(subscriber);
    }

    public long getCountHitsLimitReached() {
        return countHitsLimitReached;
    }

    public void incrementCountHitsLimitReached() {
        countHitsLimitReached++;
    }

    public DateTime getLastHitsLimitReached() {
        return lastHitsLimitReached;
    }

    public void updateLastHitsLimitReached() {
        lastHitsLimitReached = new DateTime();

    }

    public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setType(APIApplicationType type) {
		this.type = type;
	}

	public void setAccessType(APIAccessType accessType) {
		this.accessType = accessType;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public void setOrgUrl(String orgUrl) {
		this.orgUrl = orgUrl;
	}

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    /**
     * Supports APIs which require the OAuthConsumer class temporarily.
     * @return OAuthConsumer for this API client
     */
    public OAuthConsumer getOAuthConsumer() {
        return new OAuthConsumer(callback, consumerKey, secret, null);
    }
}
