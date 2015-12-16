package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.auth.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.dao.*;
import edu.upc.eetac.dsa.eventsBCN.entity.*;

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
    public Company createCompany(Company company){
        if (company==null)
            throw new BadRequestException("all parameters are mandatory");
        CompanyDAO companyDAO = new CompanyDAOImpl();
        Company c = null;
        company.setUserid(securityContext.getUserPrincipal().getName());
        try {
            System.out.println("Ahora iremos al CompanyDAOImpl");
            c = companyDAO.createCompany(company);
        } catch (CompanyAlreadyExistsException e) {
            throw new WebApplicationException("Name already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return c;
    }



    @Path("/{id}")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_COMPANY)
    public Company getCompanybyId(@PathParam("id") String id) {
        System.out.println("ID: " + id);
        Company company = null;
        try {
            company = (new CompanyDAOImpl()).getCompanyById(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (company == null)
            throw new NotFoundException("Company with id = " + id + " doesn't exist");
        return company;
    }

    @Path("/search")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_COMPANY)
    public Company getCompanybyName(@QueryParam("name") String name) {
        System.out.println("Estamos dentro!!!!");
        System.out.println(name);
        Company company = null;
        try {
            company = (new CompanyDAOImpl()).getCompanyByName(name);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (company == null)
            throw new NotFoundException("Company with name = " + name + " doesn't exist");
        return company;
    }


    @Path("/{id}")
    @PUT
    @Consumes(EventsBCNMediaType.EVENTSBCN_COMPANY)
    @Produces(EventsBCNMediaType.EVENTSBCN_COMPANY)
    public Company updateCompany(@PathParam("id") String id, Company company) {
        if(company == null)
            throw new BadRequestException("entity is null");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(company.getUserid()))
            throw new ForbiddenException("operation not allowed");
        Company c=null;
        CompanyDAO companyDAO = new CompanyDAOImpl();
        try {
            company.setId(id);
            c = companyDAO.updateCompany(company);
            if(c == null)
                throw new NotFoundException("Company with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return c;
    }


    @Path("/{id_company}/events")
    @POST
    @Consumes(EventsBCNMediaType.EVENTSBCN_EVENT)
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT)
    public Event createEvent(Event event, @PathParam("id_company") String id_company) throws URISyntaxException, SQLException {
        CompanyDAO companyDAO = new CompanyDAOImpl();
        EventDAO eventDAO = new EventDAOImpl();
        Event ev = null;

        if(event== null)
            throw new BadRequestException("all parameters are mandatory");
        if(securityContext.isUserInRole("company")==false)
            throw new ForbiddenException("You are not a company bro ;)");
        event.setCompanyid(companyDAO.companyidFromUserid(securityContext.getUserPrincipal().getName()));
        try {
            ev = eventDAO.createEvent(event);
        }catch (EventAlreadyExistsException e) {
            throw new WebApplicationException("Event with that title already exists", Response.Status.CONFLICT);
        }catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return ev;
    }


    @Path("/{id_company}/events/{id}")
    @DELETE
    public void deleteEvent(@PathParam("id_company") String c_id, @PathParam("id") String id) throws SQLException {
        CompanyDAO companyDAO = new CompanyDAOImpl();
        EventDAO eventDAO = new EventDAOImpl();

        if(securityContext.isUserInRole("company")==false)
            throw new ForbiddenException("You are not a company bro ;)");

        String companyid = companyDAO.companyidFromUserid(securityContext.getUserPrincipal().getName());
        System.out.println(companyid);
        System.out.println(c_id);
        if(c_id!=companyid) throw new ForbiddenException("Error in company id");
        try {
            if(!eventDAO.deleteEvent(id))
                throw new NotFoundException("Event with id = "+id+" doesn't exist or you didn't create this event");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Path("/{id_company}/events")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT_COLLECTION)
    public EventCollection getEventsCompany(@PathParam("id_company") String idcompany ,@QueryParam("timestamp") long timestamp, @DefaultValue("true") @QueryParam("before") boolean before) {
        EventCollection eventCollection = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {
            if (before && timestamp == 0) timestamp = System.currentTimeMillis();
            eventCollection = eventDAO.getEventsByCompany(idcompany);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return eventCollection;
    }


    @Path("/{id_company}/events/{id}")
    @PUT
    @Consumes(EventsBCNMediaType.EVENTSBCN_EVENT)
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT)
    public Event updateEvent(@PathParam("id_company") String c_id, @PathParam("id") String id, Event event) throws SQLException {
        CompanyDAO companyDAO = new CompanyDAOImpl();
        if(event == null)
            throw new BadRequestException("entity is null");
        //Comprobar q empresa ha creado ese evento (pillar empresa q queremo modificar y la id del token y concuerda con la del path
        String companyid = companyDAO.companyidFromUserid(securityContext.getUserPrincipal().getName());
        System.out.println("C_ID: "+ c_id);
        System.out.println("COMPANYID: "+ companyid);
        if(!c_id.equals(companyid)) throw new ForbiddenException("Error in company id");

        //comprovar q la compañia ha creado ese evento hacer metodo q pille la compañia de un evento
        /*Company company = new Company();
        company= companyDAO.getCompanyById(id);
        String segundacompanyid = company.getId();
        if(!segundacompanyid.equals(companyid)) throw new ForbiddenException("You haven't auth");
        */
        Event event1=null;
        EventDAO eventDAO = new EventDAOImpl();
        try {
            event.setId(id);
            event1 = eventDAO.updateEvent(event);
            if(event1 == null)
                throw new NotFoundException("Event with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event1;
    }

    @Path("/{id_company}/events/{id_event}/assist")
    @POST
    public void assistEvent(@PathParam("id_event") String idevent){
        if(securityContext.isUserInRole("registered")==false)
            throw new ForbiddenException("You are not a registered ");
        EventDAO eventDAO = new EventDAOImpl();
        Event event = null;
        try {
            event = eventDAO.getEventById(idevent);
            if(event==null)
                throw new NotFoundException("Event with id = "+idevent+" doesn't exist");
            else{
                String userid = securityContext.getUserPrincipal().getName();
                try {
                    eventDAO.assisttoEvent(userid, idevent);
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

    @Path("/{id_company}/events/{id_event}/wontassist")
    @DELETE
    public void wontassistEvent(@PathParam("id_event") String idevent){
        if(securityContext.isUserInRole("registered")==false)
            throw new ForbiddenException("You are not a registered ");
        EventDAO eventDAO = new EventDAOImpl();
        Event event = null;
        try {
            event = eventDAO.getEventById(idevent);
            if(event==null)
                throw new NotFoundException("Event with id = "+idevent+" doesn't exist");
            else{
                String userid = securityContext.getUserPrincipal().getName();
                try {
                    eventDAO.wontassit(userid, idevent);
                } catch (UserWontAssistException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
