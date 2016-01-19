package edu.upc.eetac.dsa.eventsbcn.entity;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Link;

/**
 * Created by juan on 8/12/15.
 */
public class User implements Serializable {
    private List<edu.upc.eetac.dsa.eventsbcn.entity.Link> links;
    private String id;
    private String name;
    private String password;
    private String email;
    private String photo;
    private String photoURL;
    private List<String> categories;
    private boolean isFollowed;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<edu.upc.eetac.dsa.eventsbcn.entity.Link> getLinks() {
        return links;
    }

    public void setLinks(List<edu.upc.eetac.dsa.eventsbcn.entity.Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}