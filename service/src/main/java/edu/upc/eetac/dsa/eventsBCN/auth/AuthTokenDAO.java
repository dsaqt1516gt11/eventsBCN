package edu.upc.eetac.dsa.eventsBCN.auth;

import edu.upc.eetac.dsa.eventsBCN.entity.AuthToken;

import java.sql.SQLException;

/**
 * Created by Aitor on 24/10/15.
 */
public interface AuthTokenDAO {
    public UserInfo getUserByAuthToken(String token) throws SQLException;
    public AuthToken createAuthToken(String userid) throws SQLException;
    public void deleteToken(String userid) throws  SQLException;
}
