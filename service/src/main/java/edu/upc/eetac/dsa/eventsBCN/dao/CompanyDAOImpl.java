package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.Company;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.EventCollection;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.awt.event.ComponentAdapter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * Created by Aitor on 30/11/15.
 */
public class CompanyDAOImpl implements CompanyDAO{

    @Override
    public Company createCompany(Company company) throws SQLException, CompanyAlreadyExistsException{
        Connection connection = null;
        PreparedStatement stmt = null;
        String id = null;
        try {
            Company c = getCompanyByName(company.getName());
            if (c != null)
                throw new CompanyAlreadyExistsException();
            System.out.println("No existo como empresa");
            connection = Database.getConnection();

            stmt = connection.prepareStatement(UserDAOQuery.UUID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                id = rs.getString(1);
            else
                throw new SQLException();
            stmt = connection.prepareStatement(CompanyDAOQuery.CREATE_COMPANY);
            stmt.setString(1, id);
            stmt.setString(2, company.getName());
            stmt.setString(3, company.getDescription());
            stmt.setString(4, company.getLocation());
            stmt.setFloat(5, company.getLatitude());
            stmt.setFloat(6, company.getLongitude());
            stmt.setString(7, company.getUserid());
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
                company.setLatitude(rs.getFloat("latitude"));
                company.setLongitude(rs.getFloat("longitude"));
                company.setUserid(rs.getString("userid"));
                System.out.println("Entra dentro del if de la respuesta y lo gurada todo!");
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
                company.setLatitude(rs.getFloat("latitude"));
                company.setLongitude(rs.getFloat("longitude"));
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
    public Company updateCompany(Company company) throws SQLException{
        System.out.println("Estamos dentre de updateCompany");
        Company c = null;
        System.out.println(company.getName());
        System.out.println(company.getDescription());
        System.out.println(company.getLocation());
        System.out.println(company.getLatitude());
        System.out.println(company.getLongitude());
        System.out.println(company.getId());
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.UPDATE_COMPANY);
            stmt.setString(1, company.getName());
            stmt.setString(2, company.getDescription());
            stmt.setString(3, company.getLocation());
            stmt.setFloat(4, company.getLatitude());
            stmt.setFloat(5, company.getLongitude());
            stmt.setString(6, company.getId());

            int rows = stmt.executeUpdate();
            System.out.println("ya hemos hecho la consulta!");
            if (rows == 1)
                c = getCompanyById(company.getId());
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }

        return c;
    }

    //pasa la companyid relacionada con userid
    public String companyidFromUserid(String userid) throws SQLException{
        String companyid=null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Database.getConnection();

            stmt = connection.prepareStatement(CompanyDAOQuery.GET_COMPANYID_FROM_USERID);
            stmt.setString(1, userid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
               companyid= rs.getString("id");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        }
        return companyid;
    }


}
