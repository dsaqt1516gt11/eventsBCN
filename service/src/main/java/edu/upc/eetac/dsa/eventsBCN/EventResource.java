package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.dao.EventDAO;
import edu.upc.eetac.dsa.eventsBCN.dao.EventDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by juan on 1/12/15.
 */
public class EventResource {

    @Path("companies/{id_company}/events")
    @POST
    public Response createSting(@FormParam("title") String title, @FormParam("description") String description, @FormParam("date") String date, @FormParam("photo") String photo, @FormParam("category") String category,@FormParam("companyid") String companyid, @Context UriInfo uriInfo) throws URISyntaxException {
        if(title==null || description == null || date==null || photo == null || category == null)
            throw new BadRequestException("all parameters are mandatory");
        EventDAO eventDAO = new EventDAOImpl();
        Event event = null;

        try {
            event = eventDAO.createEvent(title,description,date,photo,category,companyid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + event.getId());
        return Response.created(uri).type(EventsBCNMediaType.EVENTSBCN_EVENT).entity(event).build();
    }






}
