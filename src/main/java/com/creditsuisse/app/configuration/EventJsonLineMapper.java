package com.creditsuisse.app.configuration;

import org.json.JSONObject;
import org.springframework.batch.item.file.LineMapper;

import com.creditsuisse.app.domain.Event;

public class EventJsonLineMapper implements LineMapper<Event> {

	@Override
	public Event mapLine(String line, int lineNumber) {
		JSONObject jo = new JSONObject(line);
		Event event = null;


		return event;
	}

}