package com.creditsuisse.app.configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.creditsuisse.app.domain.Event;

public class EventProcessor implements ItemProcessor<Event, Event>{

    private static final Logger log = LoggerFactory.getLogger(EventProcessor.class);
    
    @Override
    public Event process(final Event event) {
        final Event processEvent = new Event(event.getId(), event.getState(), event.getDuration(), event.getType(), event.getHost(), event.isAlert());

        log.debug("processed EVENT : " + processEvent.toString());

        return processEvent;
    }
}
