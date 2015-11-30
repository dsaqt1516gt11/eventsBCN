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

/**
 * Created by Aitor on 25/10/15.
 */
@Path("users")
public class UserResource {
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN)
    public Response registerUser(@FormParam("name") String name, @FormParam("password") String password, @FormParam("email") String email, @FormParam("rol") String rol, @Context UriInfo uriInfo) throws URISyntaxException {
        if (name == null || password == null || email == null)
            throw new BadRequestException("all parameters are mandatory");
        UserDAO userDAO = new UserDAOImpl();
        User user = null;
        AuthToken authenticationToken = null;
        try {
            user = userDAO.createUser(name, password, email, rol);
            authenticationToken = (new AuthTokenDAOImpl()).createAuthToken(user.getId());
        } catch (UserAlreadyExistsException e) {
            throw new WebApplicationException("Name already exists", Response.Status.CONFLICT);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + user.getId());
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

    @Path("?search={id}")
    @GET
    @Produces(EventsBCNMediaType.EVENTSBCN_USER)
    public User getUserbyName(@PathParam("name") String name) {
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
        if(!id.equals(user.getId()))
            throw new BadRequestException("path parameter id and entity parameter id doesn't match");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(id))
            throw new ForbiddenException("operation not allowed");

        UserDAO userDAO = new UserDAOImpl();
        try {
            user = userDAO.updateProfile(userid, user.getName(), user.getEmail());
            if(user == null)
                throw new NotFoundException("User with id = "+id+" doesn't exist");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return user;
    }

}