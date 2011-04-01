package com.fitbit.api.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollatedUpdates {

	public long oldestVersion = Long.MAX_VALUE;
	public long newestVersion = Long.MIN_VALUE;
	
	public Map<APISubscriber, List<UpdatedResource>> updates = new HashMap<APISubscriber, List<UpdatedResource>>();

}
