package edu.upc.eetac.dsa.eventsBCN.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.eventsBCN.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aitor on 29/11/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {
    @InjectLinks({
            @InjectLink(resource = EventsBCNRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "Beeter Root API"),
            @InjectLink(resource = EventResource.class, method = "getEvent", style = InjectLink.Style.ABSOLUTE, rel = "self event", title = "Event", bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = CompanyResource.class, method = "assistEvent", style = InjectLink.Style.ABSOLUTE, rel = "assist", title = "Asistir", bindings = {@Binding(name = "id_company", value = "${instance.companyid}"), @Binding(name = "id_event", value = "${instance.id}")}),
            @InjectLink(resource = CompanyResource.class, method = "wontassistEvent", style = InjectLink.Style.ABSOLUTE, rel = "wontassist", title = " Dejar de Asistir", bindings = {@Binding(name = "id_company", value = "${instance.companyid}"), @Binding(name = "id_event", value = "${instance.id}")}),
            @InjectLink(resource = CompanyResource.class, method = "getCompanybyId", style = InjectLink.Style.ABSOLUTE, rel = "company-profile", title = "Company", bindings = @Binding(name = "id", value = "${instance.companyid}")),
            @InjectLink(resource = CompanyResource.class, method = "deleteEvent", style = InjectLink.Style.ABSOLUTE, rel = "delete", title = "delete event", bindings = {@Binding(name = "id_company", value = "${instance.companyid}"), @Binding(name = "id", value = "${instance.id}")}),




    })
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
    private boolean isAssisted;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

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
        return isAssisted;
    }

    public void setAssisted(boolean assisted) {
        isAssisted = assisted;
    }
}