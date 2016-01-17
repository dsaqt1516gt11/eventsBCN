package edu.upc.eetac.dsa.eventsBCN.entity;

import edu.upc.eetac.dsa.eventsBCN.*;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.util.List;

/**
 * Created by juan on 6/12/15.
 */
public class EventsBCNRootAPI {

    @InjectLinks({
            @InjectLink(resource = EventsBCNRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "self bookmark home", title = "EventsBCN Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "login", title = "Login",  type= EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN),
            @InjectLink(resource = UserResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-user", title = "Register", type= EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN),
            @InjectLink(resource = CompanyResource.class, style = InjectLink.Style.ABSOLUTE, rel = "create-company", title = "Register Company", type= EventsBCNMediaType.EVENTSBCN_COMPANY),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout", condition="${!empty resource.userid}"),
            @InjectLink(resource = UserResource.class, method="getUser", style = InjectLink.Style.ABSOLUTE, rel = "user-profile", title = "User profile", condition="${!empty resource.userid}", type= EventsBCNMediaType.EVENTSBCN_USER, bindings = @Binding(name = "id", value = "${resource.userid}")),
            @InjectLink(resource = CompanyResource.class, method = "getCompanybyName", style = InjectLink.Style.ABSOLUTE, rel = "company-profile-name", title = "Company"),







    })
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
