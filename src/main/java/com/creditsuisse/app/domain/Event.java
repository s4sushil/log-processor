package com.creditsuisse.app.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Event {

    @Id
    @Column (name = "identifier", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long identifier;
	
    @Column (name = "id", nullable = false)
    private final String id;

	@Column (name = "duration")
    private final Long duration;
    
    @Column (name = "state")
    private final String state;

    @Column (name = "type")
    private final String type;
    

    @Column (name = "host")
    private final String host;
    
    @Column (name = "alert")
    private final boolean alert;

    public Event(String id, String state, Long duration, String type, String host, boolean alert) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = alert;
        this.state = state;
    }

    public long getIdentifier() {
		return identifier;
	}

    public String getId() {
        return id;
    }

    public Long getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public String getState() {
        return state;
    }
    
    public boolean isAlert() {
        return alert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return alert == event.alert &&
                Objects.equals(id, event.id) &&
                Objects.equals(duration, event.duration) &&
                Objects.equals(state, event.state) &&
                Objects.equals(type, event.type) &&
                Objects.equals(host, event.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, duration, type, state, host, alert);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", state=" + state +
                ", duration=" + duration +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", alert=" + alert +
                '}';
    }
}
