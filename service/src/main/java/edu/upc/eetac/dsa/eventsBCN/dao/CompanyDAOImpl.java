package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.Company;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.awt.event.ComponentAdapter;
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
            stmt.setString(2, eventid);

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

    @Override
    public Company createCompany(String name, String description, String location, String coordinate, String userid) throws SQLException{

        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            connection = Database.getConnection();


            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();

            stmt = connection.prepareStatement(CompanyDAOQuery.CREATE_COMPANY);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.setString(4, location);
            stmt.setString(5, coordinate);
            stmt.setString(6, userid);
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
        return getCompanyById(id);
    }

    @Override
    public Company getCompanyById(String id) throws SQLException {
        Company company = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.GET_COMPANY_BY_ID);
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                company = new Company();
                company.setId(rs.getString("id"));
                company.setName(rs.getString("name"));
                company.setDescription(rs.getString("description"));
                company.setLocation(rs.getString("location"));
                company.setCoordinate(rs.getString("coordinate"));
                company.setUserid(rs.getString("userid"));


            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return company;
    }

    @Override
    public Company getCompanyByName(String name) throws SQLException {
        Company company = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.GET_COMPANY_BY_NAME);
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                company = new Company();
                company.setId(rs.getString("id"));
                company.setName(rs.getString("name"));
                company.setDescription(rs.getString("description"));
                company.setLocation(rs.getString("location"));
                company.setCoordinate(rs.getString("coordinate"));
                company.setUserid(rs.getString("userid"));


            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return company;
    }

    @Override
    public Company updateCompany(String id, String name, String description,String location,String coordinate) throws SQLException{
        Company company = null;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.UPDATE_COMPANY);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, location);
            stmt.setString(4, coordinate);
            stmt.setString(5, id);




            int rows = stmt.executeUpdate();
            if (rows == 1)
                company = getCompanyById(id);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return company;
    }
}
