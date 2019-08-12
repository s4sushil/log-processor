package com.creditsuisse.app.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.creditsuisse.app.domain.Event;

public class MapUtil {
	private static MapUtil sInstance;

	private Map<String, Event> ids;

	private MapUtil() {
		ids = new ConcurrentHashMap<>();
	}

	public synchronized static MapUtil getInstance() {
		if (sInstance == null) {
			sInstance = new MapUtil();
		}
		return sInstance;
	}

	public Map<String, Event> getIds() {
		return ids;
	}

}
