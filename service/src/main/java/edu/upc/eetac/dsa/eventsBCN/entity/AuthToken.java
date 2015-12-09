package edu.upc.eetac.dsa.eventsBCN.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.eventsBCN.EventsBCNMediaType;
import edu.upc.eetac.dsa.eventsBCN.EventsBCNRootAPIResource;
import edu.upc.eetac.dsa.eventsBCN.LoginResource;
import edu.upc.eetac.dsa.eventsBCN.UserResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by Aitor on 24/10/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthToken {
    @InjectLinks({
            @InjectLink(resource = EventsBCNRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "EventsBCN Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self login", title = "Login", type= EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = UserResource.class, method = "getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", type= EventsBCNMediaType.EVENTSBCN_USER, bindings = @Binding(name = "id", value = "${instance.userid}"))
    })
    private List<Link> links;

    private String userid;
    private String token;

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
}
