package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.sql.SQLException;

/**
 * Created by Aitor on 24/10/15.
 */
public interface UserDAO {
    public User createUser(String name, String password, String email, String role) throws SQLException, UserAlreadyExistsException;

    public User getUserById(String id) throws SQLException;

    public User getUserByName(String name) throws SQLException;

    public User updateProfile(String id, String name, String email) throws SQLException;

    public boolean checkPassword(String id, String password) throws SQLException;

}
