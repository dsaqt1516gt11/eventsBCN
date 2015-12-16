package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.auth.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.dao.*;
import edu.upc.eetac.dsa.eventsBCN.entity.AuthToken;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Aitor on 25/10/15.
 */

@Path("users")
public class UserResource {

    @POST
    @Consumes(EventsBCNMediaType.EVENTSBCN_USER)
    @Produces(EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN)
    public Response registerUser(User user, @Context UriInfo uriInfo, @QueryParam("role") String role) throws URISyntaxException {
        System.out.println(user.getCategories());
        if (user==null) {
            System.out.println("error user==null");
            throw new BadRequestException("all parameters are mandatory");
        }
        UserDAO userDAO = new UserDAOImpl();
        User u = null;
        AuthToken authenticationToken = null;
        try {
            u = userDAO.createUser(user, role);

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


    @Path("/{id}")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_USER)
    public User getUserbyId(@PathParam("id") String id) {
        User user = null;
        try {
            user = (new UserDAOImpl()).getUserById(id);
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
    public User updateUser(@PathParam("id") String id, User user) {
        if(user == null)
            throw new BadRequestException("entity is null");

        String role = null;
        if(securityContext.isUserInRole("registered")) role ="registered";
        else role="company";

        String userid = securityContext.getUserPrincipal().getName();
        System.out.println("ID:" +userid);
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");
        User u = null;
        UserDAO userDAO = new UserDAOImpl();
        try {
            user.setId(id);
            u = userDAO.updateProfile(user, role);
            System.out.println("usuario actualizado!!");
            if(u == null)
                throw new NotFoundException("User with id = "+id+" doesn't exist");
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