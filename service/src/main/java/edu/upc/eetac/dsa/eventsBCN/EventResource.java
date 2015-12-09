package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.dao.EventDAO;
import edu.upc.eetac.dsa.eventsBCN.dao.EventDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.dao.UserAlreadyAssisttoEvent;
import edu.upc.eetac.dsa.eventsBCN.dao.UserWontAssistException;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;

/**
 * Created by juan on 1/12/15.
 */
@Path("events")
public class EventResource {

    @Context
    private SecurityContext securityContext;

    @Path("/{id_event}/assist")
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

    @Path("/{id_event}/wontassist")
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


    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT_COLLECTION)
    public EventCollection getEvents(){
        EventCollection eventCollection = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {
            eventCollection = eventDAO.getEvents();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        return eventCollection;
    }

    @Path("/{id}")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT)
    public Event getEvent(@PathParam("id") String id){
        Event event = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {
            event = eventDAO.getEventById(id);
            if(event == null)
                throw new NotFoundException("Event with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event;
    }
}
