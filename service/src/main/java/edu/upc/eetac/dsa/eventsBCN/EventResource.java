package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.dao.EventDAO;
import edu.upc.eetac.dsa.eventsBCN.dao.EventDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.dao.UserAlreadyAssisttoEvent;
import edu.upc.eetac.dsa.eventsBCN.dao.UserWontAssistException;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * Created by juan on 1/12/15.
 */
@Path("events")
public class EventResource {

    @Context
    private SecurityContext securityContext;

    @Path("/test")
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
            boolean check = false;
            System.out.println("EVENTID: "+ id);
            String userid = securityContext.getUserPrincipal().getName();
            System.out.println("USERID: "+ userid);

            check = eventDAO.checkUser(userid, id);
            System.out.println("check:" + check);
            event.setAssisted(check);

            String url=null;
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
            url = prb.getString("imgBaseURLevento");
            event.setPhotoURL(url + event.getPhoto());

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        System.out.println("EL PUTO ASSIST:" + event.isAssisted());
        return event;
    }

    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT_COLLECTION)
    public EventCollection getEventsByCategoriesUser(){

        EventCollection eventCollection = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {
            String userid = securityContext.getUserPrincipal().getName();
            System.out.println("EL ID es" + userid);
            eventCollection = eventDAO.getEventsByCategories(userid);

            List<Event> events = eventCollection.getEvents();
            for( Event event : events ) {
                String url=null;
                PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
                url = prb.getString("imgBaseURLevento");
                event.setPhotoURL(url + event.getPhoto());
            }
        } catch (SQLException e) {

            throw new InternalServerErrorException();
        }

        return eventCollection;
    }
}
