package edu.upc.eetac.dsa.eventsBCN;

import edu.upc.eetac.dsa.eventsBCN.auth.AuthTokenDAO;
import edu.upc.eetac.dsa.eventsBCN.auth.AuthTokenDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.auth.UserInfo;
import edu.upc.eetac.dsa.eventsBCN.dao.UserDAO;
import edu.upc.eetac.dsa.eventsBCN.dao.UserDAOImpl;
import edu.upc.eetac.dsa.eventsBCN.entity.AuthToken;
import edu.upc.eetac.dsa.eventsBCN.entity.Role;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by Aitor on 29/11/15.
 */
@Path("login")
public class LoginResource {
    @Context
    SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(EventsBCNMediaType.EVENTSBCN_AUTH_TOKEN)
    public AuthToken login(@FormParam("login") String loginid, @FormParam("password") String password) {
        if(loginid == null || password == null)
            throw new BadRequestException("all parameters are mandatory");

        User user = null;
        AuthToken authToken = null;

        try{
            UserDAO userDAO = new UserDAOImpl();
            user = userDAO.getUserByName(loginid);
            if(user == null)
                throw new BadRequestException("loginid " + loginid + " not found.");
            if(!userDAO.checkPassword(user.getId(), password))
                throw new BadRequestException("incorrect password");

            AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
            authTokenDAO.deleteToken(user.getId());
            authToken = authTokenDAO.createAuthToken(user.getId());


        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
        return authToken;

    }

    @DELETE
    public void logout(){
        String userid = securityContext.getUserPrincipal().getName();
        AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
        try {
            authTokenDAO.deleteToken(userid);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
