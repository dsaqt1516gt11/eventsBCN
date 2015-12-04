package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.dao.EventDAO;
import edu.upc.eetac.dsa.eventsBCN.dao.EventDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by juan on 1/12/15.
 */
@Path("events")
public class EventResource {

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

    @Path("/{id}")
    @PUT
    @Consumes(EventsBCNMediaType.EVENTSBCN_EVENT)
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT)
    public Event updateEvent(@PathParam("id") String id, Event event) {

        if(event == null)
            throw new BadRequestException("entity is null");
        if(!id.equals(event.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        EventDAO eventDAO = new EventDAOImpl();
        try {
            event = eventDAO.updateEvent(id, event.getTitle(), event.getDescription(), event.getDate(), event.getPhoto(), event.getCategory());
            if(event == null)
                throw new NotFoundException("Event with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return event;
    }

    @Path("/{id}")
    @DELETE
    public void deleteEvent(@PathParam("id") String id) {

        EventDAO eventDAO = new EventDAOImpl();
        try {

            if(!eventDAO.deleteEvent(id))
                throw new NotFoundException("Event with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }








}
