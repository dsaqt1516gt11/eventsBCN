package edu.upc.eetac.dsa.eventsBCN.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Aitor on 30/11/15.
 */
public class CompanyDAOImpl implements CompanyDAO{

    @Override
    public boolean assisttoEvent(String id, String eventid) throws SQLException, UserAlreadyAssisttoEvent {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            boolean isAssisted = checkUser(id, eventid);
            if (isAssisted == true)
                throw new UserAlreadyAssisttoEvent();

            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.ASSIST_EVENT);
            stmt.setString(1, id);
            stmt.setString(2, eventid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
        return true;
    }

    @Override
    public boolean didntassit(String id, String eventid) throws SQLException, UserDidntAssistException {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {

            boolean isAssisted = checkUser(id, eventid);
            if (isAssisted != true)
                throw new UserDidntAssistException();

            System.out.println(isAssisted);
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.ABSENT_EVENT);
            stmt.setString(1, id);
            stmt.setString(2, eventid);

            int rows = stmt.executeUpdate();
            System.out.println(rows);
            if (rows == 1) return true;
            else return false;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
    }


    @Override
    public boolean checkUser(String id, String eventid) throws SQLException{
        Connection connection = null;
        PreparedStatement stmt = null;
        boolean a=false;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.COMPARE_USER_EVENT);

            stmt.setString(1, id);
            stmt.setString(2,eventid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { a=true;}
        }catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return a;
    }

}
