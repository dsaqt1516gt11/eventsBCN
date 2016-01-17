package edu.upc.eetac.dsa.eventsBCN.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import edu.upc.eetac.dsa.eventsBCN.CompanyResource;
import edu.upc.eetac.dsa.eventsBCN.EventsBCNMediaType;
import edu.upc.eetac.dsa.eventsBCN.EventsBCNRootAPIResource;
import edu.upc.eetac.dsa.eventsBCN.LoginResource;
import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.List;
/**
 * Created by Aitor on 29/11/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {
    @InjectLinks({
            @InjectLink(resource = EventsBCNRootAPIResource.class, style = InjectLink.Style.ABSOLUTE, rel = "home", title = "EventsBCN Root API"),
            @InjectLink(resource = LoginResource.class, style = InjectLink.Style.ABSOLUTE, rel = "logout", title = "Logout"),
            @InjectLink(resource = CompanyResource.class, method = "getCompanybyId", style = InjectLink.Style.ABSOLUTE, rel = "self", title = "Company profile", type= EventsBCNMediaType.EVENTSBCN_COMPANY, bindings = @Binding(name = "id", value = "${instance.id}")),
            @InjectLink(resource = CompanyResource.class, method = "getCompanybyUserID", style = InjectLink.Style.ABSOLUTE, rel = "self-by-userid", title = "Company profile by userid", type= EventsBCNMediaType.EVENTSBCN_COMPANY, bindings = @Binding(name = "userid", value = "${instance.userid}")),
            @InjectLink(resource = CompanyResource.class, method = "getEventsCompany", style = InjectLink.Style.ABSOLUTE, rel = "companyevents", title = "Company events", type= EventsBCNMediaType.EVENTSBCN_COMPANY, bindings = @Binding(name = "id_company", value = "${instance.id}")),
            @InjectLink(resource = CompanyResource.class, method = "createEvent", style = InjectLink.Style.ABSOLUTE, rel = "crear-evento", title = "Company events", type= EventsBCNMediaType.EVENTSBCN_COMPANY, bindings = @Binding(name = "id_company", value = "${instance.id}")),



    })
    private List<Link> links;
    private String id;
    private String userid;
    private String name;
    private String description;
    private String location;
    private Float latitude;
    private Float longitude;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
