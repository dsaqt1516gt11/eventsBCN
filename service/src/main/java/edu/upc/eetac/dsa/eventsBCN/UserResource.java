package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.auth.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.dao.*;
import edu.upc.eetac.dsa.eventsBCN.entity.AuthToken;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

/**
 * Created by Aitor on 25/10/15.
 */

@Path("users")
public class UserResource {

    @POST
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN)
    public Response registerUser(@FormDataParam("name") String name, @FormDataParam("password") String password, @FormDataParam("email") String email, @FormDataParam("categories") List<String> categories, @FormDataParam("image") InputStream image, @FormDataParam("image") FormDataContentDisposition fileDisposition , @Context UriInfo uriInfo, @QueryParam("role") String role) throws URISyntaxException {
        if (name==null || password==null || email==null || categories==null || role==null || image==null) {
            throw new BadRequestException("all parameters are mandatory");
        }
        User user =null;
        String url=null;
        System.out.println("CATEGORIAS: "+ categories);
        UUID uuid = writeAndConvertImage(image);
        System.out.println(fileDisposition.getFileName());
        UserDAO userDAO = new UserDAOImpl();
        AuthToken authenticationToken = null;
        try {
            System.out.println("NOMBRE DEL FICHERO: " + uuid.toString() + ".png");
            user =userDAO.createUser(name,password,email,uuid.toString() + ".png", categories,role);
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
            url = prb.getString("imgBaseURL");
            user.setPhotoURL(url + user.getPhoto());
            System.out.println("3");
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        } catch (UserAlreadyExistsException e) {
            throw new WebApplicationException("Name already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }catch (NullPointerException e){
            System.out.println(e.toString());
        }

        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
        System.out.println("torna el token a l'usuari:" + uri);
        return Response.created(uri).type(EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN).entity(authenticationToken).build();
    }

    private UUID writeAndConvertImage(InputStream file) {

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
            path = prb.getString("uploadFolder");
            System.out.println("EL DIRECTORIO ES: "+ path);
            ImageIO.write(image, "png", new File(path + filename));
        } catch (IOException e) {
            throw new InternalServerErrorException(
                    "Something has been wrong when converting the file.");
        }

        return uuid;
    }


    @Path("/assist/{id}")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_EVENT_COLLECTION)
    public EventCollection getEventsAssist(@PathParam("id") String id) {
        System.out.println("DENTRO de getEventsAssist");
        EventCollection eventCollection = null;
        EventDAO eventDAO = new EventDAOImpl();
        try {
            System.out.println("1A");
            eventCollection = eventDAO.getEventsByAssist(id);
            System.out.println("2A");
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


    @Path("/{id}")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_USER)
    public User getUserbyId(@PathParam("id") String id) {
        User user = null;
        try {
            user = (new UserDAOImpl()).getUserById(id);
            String url=null;
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
            url = prb.getString("imgBaseURL");
            user.setPhotoURL(url + user.getPhoto());
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (user == null)
            throw new NotFoundException("User with id = " + id + " doesn't exist");
        return user;
    }


    @Path("/search")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_USER)
    public User getUserbyName(@QueryParam("name") String name) {
        System.out.println("Estamos dentro!!!!");
        System.out.println(name);
        User user = null;
        try {
            user = (new UserDAOImpl()).getUserByName(name);
            String url=null;
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
            url = prb.getString("imgBaseURL");
            user.setPhotoURL(url + user.getPhoto());
        } catch (SQLException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        if (user == null)
            throw new NotFoundException("User with name = " + name + " doesn't exist");
        return user;
    }


    @Context
    private SecurityContext securityContext;
    @Path("/{id}")
    @PUT
    @Consumes(EventsBCNMediaType.EVENTSBCN_USER)
    @Produces(EventsBCNMediaType.EVENTSBCN_USER)
    public User updateUser(@PathParam("id") String id, @FormDataParam("name") String name, @FormDataParam("password") String password, @FormDataParam("email") String email, @FormDataParam("categories") List<String> categories, @FormDataParam("image") InputStream image, @FormDataParam("image") FormDataContentDisposition fileDisposition) {
        if(name==null || password==null || email==null || categories==null || image==null )
            throw new BadRequestException("entity is null");

        String role = null;
        if(securityContext.isUserInRole("registered")) role ="registered";
        else role="company";
        UUID uuid = writeAndConvertImage(image);
        String userid = securityContext.getUserPrincipal().getName();
        System.out.println("ID:" +userid);
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        User user = null;
        User u = null;
        UserDAO userDAO = new UserDAOImpl();
        try {
            user.setId(id);
            user.setName(name);
            user.setPassword(password);
            user.setPhoto(uuid.toString());
            user.setEmail(email);
            u = userDAO.updateProfile(user, role);
            System.out.println("usuario actualizado!!");
            if(u == null)
                throw new NotFoundException("User with id = "+id+" doesn't exist");
            String url=null;
            PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("eventsBCN");
            url = prb.getString("imgBaseURL");
            u.setPhotoURL(url + u.getPhoto());
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return u;
    }

    @Path("/{id_follow}/follow")
    @POST
    public void userFollow(@PathParam("id_follow") String followid){

        String userid = securityContext.getUserPrincipal().getName();

        UserDAO userDAO = new UserDAOImpl();
        try {
            userDAO.userFollow(followid,userid);

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserAlreadyFollowedException e) {
            throw new BadRequestException("Ya sigues a esta persona");
        }

    }

    @Path("/{id_follow}/unfollow")
    @POST
    public void userUnfollow(@PathParam("id_follow") String followid){

        String userid = securityContext.getUserPrincipal().getName();

        UserDAO userDAO = new UserDAOImpl();
        try {
            userDAO.userUnfollow(followid,userid);

        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserNotFollowedException e) {
            throw new NotFoundException("No sigues a esta persona");
        }

    }
}