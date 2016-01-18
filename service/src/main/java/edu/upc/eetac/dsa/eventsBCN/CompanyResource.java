package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.dao.*;
import edu.upc.eetac.dsa.eventsBCN.entity.*;
import org.glassfish.grizzly.utils.EchoFilter;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;

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
            throw new ForbiddenException("Company with name = " + name + " doesn't exist");
        return company;
    }

    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_COMPANY)
    public Company getCompanybyUserID(@QueryParam("userid") String userid) {
        CompanyDAO companyDAO = new CompanyDAOImpl();
        Company c = null;
        try {
            c = companyDAO.getCompanyById(companyDAO.companyidFromUserid(userid));
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (c == null)
            throw new NotFoundException("Company with name = " + userid + " doesn't exist");
        return c;
    }

    @Path("/{id}")
    @PUT
    @Consumes(EventsBCNMediaType.EVENTSBCN_COMPANY)
    @Produces(EventsBCNMediaType.EVENTSBCN_COMPANY)
    public Company updateCompany(@PathParam("id") String id, Company company) {
        if(company == null)
            throw new BadRequestException("entity is null");

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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT)
    public Event createEvent(@FormDataParam("title") String title, @FormDataParam("description") String description, @FormDataParam("date") String date, @FormDataParam("image") InputStream image, @FormDataParam("image") FormDataContentDisposition fileDisposition, @FormDataParam("category") String category, @PathParam("id_company") String id_company) throws URISyntaxException, SQLException {
        CompanyDAO companyDAO = new CompanyDAOImpl();
        EventDAO eventDAO = new EventDAOImpl();
        Event ev = null;
        UUID uuid = writeAndConvertImage(image);
        System.out.println("LLEGO A ENTRAR");
        if(title== null ||description== null ||date== null ||image== null ||category== null)
            throw new BadRequestException("all parameters are mandatory");
        if(securityContext.isUserInRole("company")==false)
            throw new ForbiddenException("You are not a company bro ;)");
        System.out.println("PETO3");
        Event event = new Event();
        event.setTitle(title);
        System.out.println(title);
        event.setDescription(description);
        System.out.println(description);
        event.setDate(date);
        System.out.println(date);
        event.setCategory(category);
        System.out.println(category);
        System.out.println("PETO_IMAGEN");
        event.setPhoto(uuid.toString() + ".png");
        System.out.println("PETO0");
        event.setCompanyid(companyDAO.companyidFromUserid(securityContext.getUserPrincipal().getName()));
        System.out.println("PETO1");
        try {
            ev = eventDAO.createEvent(event);
            System.out.println("PETO2");
            String url=null;
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
            url = prb.getString("imgBaseURLevento");
            ev.setPhotoURL(url + ev.getPhoto());
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
            throw new NotAuthorizedException("You are not a company bro ;)");

        String companyid = companyDAO.companyidFromUserid(securityContext.getUserPrincipal().getName());
        System.out.println(companyid);
        System.out.println(c_id);
        if(!c_id.equals(companyid)) throw new ForbiddenException("Error in company id");
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
    public EventCollection getEventsCompany(@PathParam("id_company") String idcompany) {
        EventCollection eventCollection = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {

            eventCollection = eventDAO.getEventsByCompany(idcompany);

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

    private UUID writeAndConvertImage(InputStream file) {
        System.out.println("EN EL WRITE");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);

        } catch (IOException e) {
            throw new InternalServerErrorException(
                    "Something has been wrong when reading the file.");
        }
        UUID uuid = UUID.randomUUID();
        System.out.println("NOMBRE QUE QUIERE PONER AL FICHERO" + uuid.toString());
        String filename = uuid.toString() + ".png";
        System.out.println(filename);
        try {
            String path=null;
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
            path = prb.getString("uploadFolderEvents");
            System.out.println("EL DIRECTORIO ES: "+ path);
            ImageIO.write(image, "png", new File(path + filename));
        } catch (IOException e) {
            throw new InternalServerErrorException(
                    "Something has been wrong when converting the file.");
        }

        return uuid;
    }
}
