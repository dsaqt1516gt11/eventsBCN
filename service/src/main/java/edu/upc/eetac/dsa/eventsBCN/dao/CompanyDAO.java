package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.Company;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.sql.SQLException;

/**
 * Created by Aitor on 30/11/15.
 */
public interface CompanyDAO {

    public boolean assisttoEvent(String id, String eventid) throws SQLException, UserAlreadyAssisttoEvent;
    public boolean didntassit(String id, String eventid) throws SQLException, UserDidntAssistException;
    public boolean checkUser(String id, String eventid) throws SQLException;
    public Company createCompany(String name, String description, String location, String coordinate, String userid) throws SQLException;
    public Company getCompanyById(String id) throws SQLException;
    public Company getCompanyByName(String name) throws SQLException;
    public Company updateCompany(String id, String name, String description,String location,String coordinate) throws SQLException;
    }

