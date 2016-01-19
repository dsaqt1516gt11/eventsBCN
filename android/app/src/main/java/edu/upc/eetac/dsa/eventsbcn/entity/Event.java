package edu.upc.eetac.dsa.eventsbcn.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan on 8/12/15.
 */
public class Event {

    private List<Link> links;
    private String id;
    private String title;
    private String description;
    private String date;
    private String photo;
    private String photoURL;
    private String category;
    private String companyid;
    private long lastModified;
    private long creationTimestamp;
    private List<User> users = new ArrayList<>();
    private boolean assisted;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isAssisted() {
        return assisted;
    }

    public void setAssisted(boolean assisted) {
        this.assisted = assisted;
    }
}
