package com.creditsuisse.app.configuration;

import org.json.JSONObject;
import org.springframework.batch.item.file.LineMapper;

import com.creditsuisse.app.domain.Event;

public class EventJsonLineMapper implements LineMapper<Event> {

	@Override
	public Event mapLine(String line, int lineNumber) {
		JSONObject jo = new JSONObject(line);
		Event event = null;
		long duration = jo.optLong("timestamp");
		String id = jo.getString("id");

		if (duration != 0 && id != null) {

			synchronized (this) {
				
				Event checkEvent = MapUtil.getInstance().getIds().get(id);
				if (checkEvent != null) {
					boolean alert = Math.abs(duration - checkEvent.getDuration()) > 4 ? true : false;
					event = extractEvent(jo, alert);
					MapUtil.getInstance().getIds().remove(id);
				} else {
					event = extractEvent(jo, false);
					MapUtil.getInstance().getIds().put(id, event);
				}
				
			}
		}

		return event;
	}

	private Event extractEvent(JSONObject jo, boolean alert) {
		Event event = null;
		if (!jo.isNull("type")) {
			event = new Event(jo.getString("id"), jo.getString("state"), jo.getLong("timestamp"), jo.getString("type"),
					jo.getString("host"), alert);
		} else {
			event = new Event(jo.getString("id"), jo.getString("state"), jo.getLong("timestamp"), null, null, alert);
		}
		return event;
	}
}