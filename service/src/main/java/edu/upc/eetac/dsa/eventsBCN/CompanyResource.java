package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.auth.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.dao.*;
import edu.upc.eetac.dsa.eventsBCN.entity.AuthToken;
import edu.upc.eetac.dsa.eventsBCN.entity.Company;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Juan on 30/11/15.
 */
@Path("companies")
public class CompanyResource {
    @Context
    private SecurityContext securityContext;
    @POST
    @Consumes(EventsBCNMediaType.EVENTSBCN_COMPANY)
    @Produces(EventsBCNMediaType.EVENTSBCN_COMPANY)
    public Response registerUser(Company company, @Context UriInfo uriInfo) throws URISyntaxException {
        if (user==null)
            throw new BadRequestException("all parameters are mandatory");
        UserDAO userDAO = new UserDAOImpl();
        User u = null;
        AuthToken authenticationToken = null;
        try {

            u = userDAO.createUser(user);

            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        } catch (UserAlreadyExistsException e) {
            throw new WebApplicationException("Name already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        System.out.println("torna el token a l'usuari:" + uri);
        return Response.created(uri).type(EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN).entity(authenticationToken).build();
    }



    //Probar
    @Path("/{id_company}/events/{id_event}/assist")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void assistEvent(@PathParam("id_company") String idcompany,@PathParam("id_event") String idevent){


        CompanyDAO companyDAO = new CompanyDAOImpl();
        EventDAO eventDAO = new EventDAOImpl();
        Event event = null;
        Company company = null;
        try {
            company = companyDAO.getCompanyById(idcompany);
            event = eventDAO.getEventById(idevent);
            if(company==null)
                throw new NotFoundException("Company with id = "+idcompany+" doesn't exist");
            else if(event==null)
                throw new NotFoundException("Event with id = "+idevent+" doesn't exist");
            else{
                String userid = securityContext.getUserPrincipal().getName();
                try {
                    companyDAO.assisttoEvent(userid, idevent);
                }
                catch (UserAlreadyAssisttoEvent e){
                    throw new ForbiddenException();
                }
            }


        }
        catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("/{id_company}/events")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createEvent(@FormParam("title") String title, @FormParam("description") String description, @FormParam("date") String date, @FormParam("photo") String photo, @FormParam("category") String category,@PathParam("id_company") String companyid, @Context UriInfo uriInfo) throws URISyntaxException {

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
