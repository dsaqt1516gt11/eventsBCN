package edu.upc.eetac.dsa.eventsBCN.dao;

import edu.upc.eetac.dsa.eventsBCN.entity.Company;
import edu.upc.eetac.dsa.eventsBCN.entity.Event;
import edu.upc.eetac.dsa.eventsBCN.entity.User;

import java.sql.SQLException;

/**
 * Created by Aitor on 30/11/15.
 */
public interface CompanyDAO {
    public Company createCompany(Company company) throws SQLException, CompanyAlreadyExistsException;
    public Company getCompanyById(String id) throws SQLException;
    public Company getCompanyByName(String name) throws SQLException;
    public Company updateCompany(Company company) throws SQLException;
    public String companyidFromUserid(String userid) throws SQLException;
    }

