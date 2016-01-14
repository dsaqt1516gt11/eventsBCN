package edu.upc.eetac.dsa.eventsbcn.entity;

import java.util.List;

/**
 * Created by juan on 6/12/15.
 */
public class AuthToken {

    private List<Link> links;
    private String userid;
    private String token;
    private String role;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
