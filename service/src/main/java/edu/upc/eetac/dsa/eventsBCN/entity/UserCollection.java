package edu.upc.eetac.dsa.eventsBCN.entity;

import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 11/12/15.
 */
public class UserCollection {
    @InjectLinks({

    })
    private List<Link> links;
    private List<User> users = new ArrayList<>();


    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<Event> events) {
        this.users = users;
    }
}
