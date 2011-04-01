package com.fitbit.api.model;

public enum EventType {
	FOOD(EntitySessionType.SHARDED),
	BODY(EntitySessionType.SHARDED),
	ACTIVITY(EntitySessionType.SHARDED),
	SLEEP(EntitySessionType.SHARDED);
	
	private EntitySessionType sessionType;
	
	private EventType(EntitySessionType sessionType) {
		this.sessionType = sessionType;
	}
	
	public EntitySessionType getSessionType() {
		return sessionType;
	}
	
	public static enum EntitySessionType {
		SHARDED,
		UNSHARDED
	}
}
