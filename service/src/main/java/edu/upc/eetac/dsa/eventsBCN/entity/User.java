package edu.upc.eetac.dsa.eventsBCN.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.eventsBCN.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by Aitor on 23/10/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @InjectLinks({
            @InjectLink(resource = EventsBCNRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "EventsBCN Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = UserResource.class, method = "getUserbyId", style = InjectLink.Style.ABSOLUTE, rel = "self", title = "User profile", type= EventsBCNMediaType.EVENTSBCN_USER, bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = UserResource.class, method = "getEventsAssist", style = InjectLink.Style.ABSOLUTE, rel = "event-assist", title = "User events assist", type= EventsBCNMediaType.EVENTSBCN_USER, bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = CompanyResource.class, method = "getCompanybyUserID", style = InjectLink.Style.ABSOLUTE, rel = "self-by-userid", title = "Company profile by userid", type= EventsBCNMediaType.EVENTSBCN_COMPANY, bindings = @Binding(name = "userid", value = "${instance.id}")),

    })
    private List<Link> links;
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
