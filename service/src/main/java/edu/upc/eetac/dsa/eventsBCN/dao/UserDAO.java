package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Aitor on 24/10/15.
 */
public interface UserDAO {
    public User createUser(User user)  throws SQLException, UserAlreadyExistsException;

    public User getUserById(String id) throws SQLException;

    public User getUserByName(String name) throws SQLException;

    public User updateProfile(String id, String name, String email, String photo, List<String> categories) throws SQLException;

    public boolean checkPassword(String id, String password) throws SQLException;

}
