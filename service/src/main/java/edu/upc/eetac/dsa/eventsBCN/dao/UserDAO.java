package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Aitor on 24/10/15.
 */
public interface UserDAO {
    public User createUser(User user, String role)  throws SQLException, UserAlreadyExistsException;

    public User getUserById(String id) throws SQLException;

    public User getUserByName(String name) throws SQLException;

    public User updateProfile(User user) throws SQLException;

    public boolean checkPassword(String id, String password) throws SQLException;

    public boolean userFollow(String userfollowid, String userfollowerid) throws SQLException,UserAlreadyFollowedException;

    public boolean userUnfollow(String userfollowid, String userfollowerid) throws SQLException,UserNotFollowedException;

    public boolean checkFollow(String referenceid, String followerid) throws SQLException;



}
