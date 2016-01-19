package edu.upc.eetac.dsa.eventsbcn.entity;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.*;

/**
 * Created by juan on 8/12/15.
 */
public class EventCollection {

    private List<Link> links;
    private long newestTimestamp;
    private long oldestTimestamp;
    private List<Event> events = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
